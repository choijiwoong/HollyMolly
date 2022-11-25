package com.holly.molly.service;

import com.holly.molly.domain.Accept;
import com.holly.molly.domain.AcceptStatus;
import com.holly.molly.domain.Request;
import com.holly.molly.domain.RequestStatus;
import lombok.RequiredArgsConstructor;
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

    //@Async
    public void checkVolunStatus(){
        for(int i=0; i<100000; i++) {
            //수행 대기중인 accept를 전부 가져온다.
            List<Accept> accepts = acceptService.findByStatus(AcceptStatus.REGISTER);
            for (Accept accept : accepts) {
                Request request = accept.getRequest();
                if (request.getStatus() == RequestStatus.ACCEPT) {
                    if (isEmailTime(request.getExectime())) {
                        sendEmail(request.getUserR().getEmail());
                        sendEmail(accept.getUserA().getEmail());
                    }

                    if (isComplete(request.getExectime())) {
                        request.setStatus(RequestStatus.COMPLETE);
                        accept.setStatus(AcceptStatus.COMPLETE);
                    }
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

    private Boolean isComplete(LocalDateTime time){//
        if(time.isBefore(LocalDateTime.now())){
            return true;
        } else {
            return false;
        }
    }

    private Boolean sendEmail(String email){//
        return true;
    }
}
