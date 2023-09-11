package com.holly.molly.service;

import com.holly.molly.DTO.AdvanceMailDTO;
import com.holly.molly.DTO.EmergencyDTO;
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

    private final String DOMAIN_ADDRESS="http://localhost:8080";

    @Transactional
    public void join(){//전체 로직 실행
        for(int i=0; i<1; i++){
            List<Request> requests=requestService.findAll();
            List<Accept> accepts=acceptService.findAll();//모든 Request와 Accept정보를 가져온다.
            checkNoticeMailTiming(accepts);//봉사시간까지 24시간 이내 ACCEPT상태의 Request, REGISTER상태의 Accept들에게 메일발송 후 MAILED로 변경

            for(Request request: requests){
                checkCancelCondition(request);//봉사자가 나타나지 않은 Request가 마감되면 자동 취소처리
            }

            for(Accept accept: accepts){
                checkCancleStatus(accept);//requestor가 취소시 acceptor도 취소처리. 반대로 acceptor는 취소시 자동 requestor취소이기에 별도처리X(의존성)
                checkRunning(accept);//exectime지나면 RUNNING처리
                checkComplete(accept);//exectime+duration지나면 COMPLETE처리
                checkReviewTiming(accept);//exectime의 day가 지나면 리뷰url이 담긴 메일 전송
            }
        }
    }

    @Transactional
    public void checkCancelCondition(Request request){//봉사자가 나타나지 않은 경우 자동 취소처리
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

    @Transactional
    public void checkNoticeMailTiming(List<Accept> accepts){//봉사예정일로부터 하루 전일 경우 메일전송
        ArrayList<AdvanceMailDTO> advanceMailDTOArrayList=new ArrayList<>();
        for (Accept accept : accepts) {
            Request request = accept.getRequest();

            if (request.getStatus() == RequestStatus.ACCEPT && isEmailTime(request.getExectime())) {
                AdvanceMailDTO advanceMailDTO=new AdvanceMailDTO(
                        request.getUserR().getEmail(),
                        accept.getUserA().getEmail(),
                        //"start",
                        //"end",
                        DOMAIN_ADDRESS+"/request/emergency/"+request.getId()
                );
                advanceMailDTOArrayList.add(advanceMailDTO);
                accept.changeStatus(AcceptStatus.MAILED);//메일 상태변경
            }

        }
        if(advanceMailDTOArrayList.isEmpty())
            return;
        mailService.advanceNotice(advanceMailDTOArrayList);
    }

    @Transactional
    public void checkRunning(Accept accept) {//봉사예정시각이 지나면 Status를 RUNNING으로 바꿈
        Request request = accept.getRequest();
        if (accept.getStatus()==AcceptStatus.MAILED && isRunning(request.getExectime())){//봉사활동시간이 지나면 COMPLETE처리
            accept.changeStatus(AcceptStatus.RUNNING);
        }
    }

    @Transactional
    public void checkComplete(Accept accept) {//봉사시간인 지나면 RUNNING에서 COMPLETE로 바꿈 
            if(accept.getStatus()!=AcceptStatus.RUNNING)//정상 대기상태가 아닌 경우
                return;

            Request request = accept.getRequest();
            System.out.println("[DEBUG]2 "+request.getStatus()+", "+accept.getStatus());
            if (isComplete(request)) {//봉사활동시간이 지나면 COMPLETE처리
                accept.changeStatus(AcceptStatus.COMPLETE);
                System.out.println("[DEBUG]3 "+request.getStatus()+", "+accept.getStatus());
            }
    }

    @Transactional
    public void checkReviewTiming(Accept accept){//봉사시작 24시간 후 리뷰요청 메일전송
        if(accept.getStatus()==AcceptStatus.COMPLETE && isReviewTime(accept.getRequest().getExectime())){
            mailService.requestReview(accept.getRequest().getUserR().getEmail(), accept.getId(), false);
            mailService.requestReview(accept.getUserA().getEmail(), accept.getRequest().getId(), true);
        }
    }

    @Transactional
    public void emergency(Long requestId) {//응급일 경우 관리자에게 응급메일 전송 및 EMERGENCY처리
        Request request=requestService.findOne(requestId).get();
        if(request.getStatus()!=RequestStatus.RUNNING)//허위신고방지, 진행중인 것만
            return;
        request.getAccept().changeStatus(AcceptStatus.EMERGENCY);
        EmergencyDTO emergencyDTO=new EmergencyDTO(requestId, request.getExectime().toString(), request.getAddress(), request.getUserR().getPhone(), request.getAccept().getUserA().getPhone());
        mailService.emergencyMail(emergencyDTO);
    }

    //<---- 하위 내부 로직 ---->
    private Boolean isEmailTime(LocalDateTime exectime){//하루전
        Long dayUnit=1l;
        exectime.plusDays(dayUnit);

        return exectime.isAfter(LocalDateTime.now()) ? true : false;
    }

    private Boolean isReviewTime(LocalDateTime exectime){//하루뒤
        LocalDateTime dayUnit=LocalDateTime.of(exectime.getYear(), exectime.getMonth(), exectime.plusDays(1).getDayOfMonth(),0,0);

        return dayUnit.isBefore(LocalDateTime.now()) ? true: false;
    }

    private Boolean isRunning(LocalDateTime exectime){//실행시간
        return exectime.isBefore(LocalDateTime.now()) ? true : false;
    }
    private Boolean isComplete(Request request){//봉사활동시간 지나면 완료
        return request.getExectime()
                .plusHours(Long.parseLong(request.getDuration()))
                .isBefore(LocalDateTime.now()) ? true : false;
    }
}
