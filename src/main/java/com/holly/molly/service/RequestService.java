package com.holly.molly.service;

import com.holly.molly.domain.Request;
import com.holly.molly.domain.RequestStatus;
import com.holly.molly.domain.User;
import com.holly.molly.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;
    @Transactional//필요시에만 readOnly=false
    public Long join(Request request){
        validateDuplicateRequest(request);
        checkExectime(request);
        requestRepository.save(request);
        return request.getId();
    }

    private void checkExectime(Request request) {
        if(request.getExectime().isBefore(LocalDateTime.now().minusMinutes(1l))){//-1m하는 이유는, LocalDateTime비교식이 너무 정밀하여 일부러 오차범위를 만듬.(for asyncservice.checkCancelCondition)
            throw new IllegalStateException("수행시간이 현재시간보다 이전입니다.");
        }
    }

    private void validateDuplicateRequest(Request request) {//request를 보낸 유저의 기존 요청목록에 수행시간, 장소가 일치하는 항목이 이미 있는지 확인한다(유저기반)
        if(!this.findByUser(request.getUserR()).stream().filter(r->r.getExectime().equals(request.getExectime())&r.getAddress().equals(request.getAddress())).toList().isEmpty()){
            throw new IllegalStateException("이미 존재하는 봉사요청입니다.");
        }
    }

    public Request findOne(Long id){
        return requestRepository.findOne(id);
    }

    public List<Request> findByUser(User user){
        return requestRepository.findByUser(user);
    }

    public List<Request> findByStatus(RequestStatus requestStatus){
        return requestRepository.findByStatus(requestStatus);
    }

    public List<Long> findKakaomapList(){
        List<Request> requests=requestRepository.findByStatus(RequestStatus.REGISTER);

        ArrayList<Long> ids=new ArrayList<>();
        for(Request request: requests){
            ids.add(request.getId());
        }

        return ids.stream().toList();
    }

    public List<Request> findAll(){
        return requestRepository.findAll();
    }
}
