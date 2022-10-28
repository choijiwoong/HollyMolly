package com.holly.molly.service;

import com.holly.molly.domain.Request;
import com.holly.molly.domain.RequestStatus;
import com.holly.molly.domain.User1;
import com.holly.molly.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;
    @Transactional//필요시에만 readOnly=false
    public Long join(Request request){
        requestRepository.save(request);
        return request.getId();
    }

    public Request findOne(Long id){
        return requestRepository.findOne(id);
    }

    public List<Request> findByUser1(User1 user){
        return requestRepository.findByUser1(user);
    }

    public List<Request> findByStatus(RequestStatus requestStatus){
        return requestRepository.findByStatus(requestStatus);
    }

    public List<Request> findByReqtime(LocalDateTime localDateTime){
        return requestRepository.findByReqtime(localDateTime);
    }

    public List<Request> findAll(){
        return requestRepository.findAll();
    }
}
