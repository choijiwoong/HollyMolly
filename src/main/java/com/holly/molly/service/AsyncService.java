package com.holly.molly.service;

import com.holly.molly.domain.Accept;
import com.holly.molly.domain.AcceptStatus;
import com.holly.molly.domain.Request;
import com.holly.molly.domain.RequestStatus;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.asm.Advice;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AsyncService {
    private final AcceptService acceptService;
    private final RequestService requestService;
    private final MailService mailService;

    public void join(){
        List<Accept> accepts=acceptService.findAll();
        List<Request> requests=requestService.findAll();
        for(int i=0; i<1; i++){
            checkCancelCondition(requests);//Request의 exectime이 지나는 경우 자동 cancel처리
            checkCancleStatus(accepts);//Accept는 Register인데 연결된 Request가 cancel인 경우 accept도 cancel처리
            checkNoticeMailTiming(accepts);//봉사 하루전 알림메일 전송
            checkComplete(accepts);//exectime이 지나면 complete처리
            checkReviewTiming(accepts);//exectime의 day가 지나면 리뷰url이 담긴 메일 전송
        }
    }

    @Transactional
    public void checkCancelCondition(List<Request> requests){
        for(Request request : requests){
            if(request.getStatus()==RequestStatus.REGISTER && request.getExectime().isBefore(LocalDateTime.now())){
                request.setStatus(RequestStatus.CANCEL);
            }
        }
    }
    @Transactional
    public void checkCancleStatus(List<Accept> accepts){
        for (Accept accept : accepts) {
            if(accept.getStatus() != AcceptStatus.REGISTER)
                continue;

            if (accept.getRequest().getStatus() == RequestStatus.CANCEL) {//요청자 취소시
                accept.setStatus(AcceptStatus.CANCEL);
            }
        }
    }

    public void checkNoticeMailTiming(List<Accept> accepts){
        for (Accept accept : accepts) {
            Request request = accept.getRequest();

            if (request.getStatus() == RequestStatus.ACCEPT && isEmailTime(request.getExectime())) {
                mailService.advanceNotice(request.getUserR().getEmail());
                mailService.advanceNotice(accept.getUserA().getEmail());
            }
        }
    }

    @Transactional
    public void checkComplete(List<Accept> accepts) {
        for (Accept accept : accepts) {
            if(accept.getStatus()==AcceptStatus.COMPLETE)//이미 처리된 경우
                continue;

            Request request = accept.getRequest();
            if (isComplete(request.getExectime())) {//봉사활동시간이 지나면 COMPLETE처리
                request.setStatus(RequestStatus.COMPLETE);
                accept.setStatus(AcceptStatus.COMPLETE);
            }
        }
    }

    public void checkReviewTiming(List<Accept> accepts){
        for(Accept accept : accepts){
            if(accept.getStatus()==AcceptStatus.COMPLETE && isReviewTime(accept.getRequest().getExectime())){
                mailService.requestReview(accept.getRequest().getUserR().getEmail(), accept.getId(), false);
                mailService.requestReview(accept.getUserA().getEmail(), accept.getRequest().getId(), true);
            }
        }
    }

    //<---- 하위 내부 로직 ---->
    private Boolean isEmailTime(LocalDateTime time){
        Long dayUnit=1l;
        time.plusDays(dayUnit);
        if(time.isAfter(LocalDateTime.now())){//차이가 하루 미만이라면
            return true;
        } else{
            return false;
        }
    }

    private Boolean isReviewTime(LocalDateTime time){
        return true;
        /*
        Long dayUnit=1l;
        time.minusHours(time.getHour());
        time.minusMinutes(time.getMinute());
        time.minusSeconds(time.getSecond());
        time.plusDays(dayUnit);
        if(time.isBefore(LocalDateTime.now())){//봉사활동 일이 마무리되는 00시 00분이 지났는지
            return true;
        } else{
            return false;
        }
        */
    }

    private Boolean isComplete(LocalDateTime time){//
        if(time.isBefore(LocalDateTime.now())){
            return true;
        } else {
            return false;
        }
    }
}
