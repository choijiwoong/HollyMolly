package com.holly.molly.service;

import com.holly.molly.domain.Accept;
import com.holly.molly.domain.AcceptStatus;
import com.holly.molly.domain.Request;
import com.holly.molly.domain.RequestStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AsyncService {
    private final AcceptService acceptService;
    private final RequestService requestService;
    private final MailService mailService;

    @Transactional
    public void join(){
        for(int i=0; i<1; i++){
            List<Request> requests=requestService.findAll();
            List<Accept> accepts=acceptService.findAll();

            for(Request request: requests){
                checkCancelCondition(request);//Request의 exectime이 지나는 경우 자동 cancel처리
                checkVolunStart(request);
            }

            for(Accept accept: accepts){
                checkCancleStatus(accept);//Accept는 Register인데 연결된 Request가 cancel인 경우 accept도 cancel처리
                checkComplete(accept);//exectime이 지나면 complete처리
                checkReviewTiming(accept);//exectime의 day가 지나면 리뷰url이 담긴 메일 전송
            }

            checkNoticeMailTiming(accepts);//하루전 메일전송
        }
    }

    @Transactional
    public void checkVolunStart(Request request){
    }

    @Transactional
    public void checkCancelCondition(Request request){
        if(request.getStatus()==RequestStatus.REGISTER && request.getExectime().isBefore(LocalDateTime.now()))
            request.changeStatus(RequestStatus.CANCEL);
    }
    @Transactional
    public void checkCancleStatus(Accept accept){//동록상태인 봉사를 피봉사자가 요청취소했을 경우
        if(accept.getStatus()!=AcceptStatus.REGISTER)//등록상태외의 경우는 취소 불가(취소, 완료, 진행)
            return;
        if (accept.getRequest().getStatus() == RequestStatus.CANCEL)//요청자 취소시
            accept.changeStatus(AcceptStatus.CANCEL);
    }

    public void checkNoticeMailTiming(List<Accept> accepts){
        ArrayList<String> emailList=new ArrayList<String>();
        for (Accept accept : accepts) {
            Request request = accept.getRequest();

            if (request.getStatus() == RequestStatus.ACCEPT && isEmailTime(request.getExectime())) {
                emailList.add(request.getUserR().getEmail());
                emailList.add(accept.getUserA().getEmail());
            }
        }
        if(emailList.isEmpty())
            return;
        mailService.advanceNotice(emailList);
    }

    @Transactional
    public void checkComplete(Accept accept) {
            if(accept.getStatus()!=AcceptStatus.REGISTER)//정상 대기상태가 아닌 경우
                return;

            Request request = accept.getRequest();
            if (isComplete(request.getExectime())) {//봉사활동시간이 지나면 COMPLETE처리
                request.changeStatus(RequestStatus.COMPLETE);
                accept.changeStatus(AcceptStatus.COMPLETE);
            }
    }

    public void checkReviewTiming(Accept accept){
        if(accept.getStatus()==AcceptStatus.COMPLETE && isReviewTime(accept.getRequest().getExectime())){
            mailService.requestReview(accept.getRequest().getUserR().getEmail(), accept.getId(), false);
            mailService.requestReview(accept.getUserA().getEmail(), accept.getRequest().getId(), true);
        }
    }

    //<---- 하위 내부 로직 ---->
    private Boolean isEmailTime(LocalDateTime exectime){
        Long dayUnit=1l;
        exectime.plusDays(dayUnit);

        return exectime.isAfter(LocalDateTime.now()) ? true : false;
    }

    private Boolean isReviewTime(LocalDateTime exectime){
        LocalDateTime dayUnit=LocalDateTime.of(exectime.getYear(), exectime.getMonth(), exectime.plusDays(1).getDayOfMonth(),0,0);

        return dayUnit.isBefore(LocalDateTime.now()) ? true: false;
    }

    private Boolean isComplete(LocalDateTime exectime){
        return exectime.isBefore(LocalDateTime.now()) ? true : false;
    }
}
