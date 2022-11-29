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

    //@Async
    @Transactional
    public void checkVolunStatus(){
        for(int i=0; i<1; i++) {//임시!
            //accept 베이스 처리
            List<Accept> accepts = acceptService.findByStatus(AcceptStatus.REGISTER);
            for (Accept accept : accepts) {
                Request request = accept.getRequest();

                if(request.getStatus()==RequestStatus.CANCEL){//요청자 취소시
                    accept.setStatus(AcceptStatus.CANCEL);
                }

                if (request.getStatus() == RequestStatus.ACCEPT) {
                    if (isEmailTime(request.getExectime())) {//봉사활동 예정일 하루 전 이메일 전송
                        mailService.advanceNotice(request.getUserR().getEmail());
                        mailService.advanceNotice(accept.getUserA().getEmail());
                    }

                    if (isComplete(request.getExectime())) {//봉사활동시간이 지나면 COMPLETE처리
                        request.setStatus(RequestStatus.COMPLETE);
                        accept.setStatus(AcceptStatus.COMPLETE);
                    }
                }
            }

            //request 베이스 처리
            List<Request> requests=requestService.findByStatus(RequestStatus.REGISTER);
            for(Request request : requests){
                if(request.getExectime().isBefore(LocalDateTime.now())){
                    request.setStatus(RequestStatus.CANCEL);
                }
            }

            //Volun 베이스 처리
            List<Accept> voluns=acceptService.findByStatus(AcceptStatus.COMPLETE);
            for(Accept volun : voluns){
                if(isReviewTime(volun.getRequest().getExectime())){
                    mailService.requestReview(volun.getRequest().getUserR().getEmail(), volun.getId(), false);
                    mailService.requestReview(volun.getUserA().getEmail(), volun.getRequest().getId(), true);
                }
            }
        }
    }

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
    }

    private Boolean isComplete(LocalDateTime time){//
        if(time.isBefore(LocalDateTime.now())){
            return true;
        } else {
            return false;
        }
    }
}
