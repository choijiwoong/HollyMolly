package com.holly.molly.service;

import com.holly.molly.domain.Request;
import com.holly.molly.domain.RequestStatus;
import com.holly.molly.domain.User;
import com.holly.molly.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;
    @Transactional
    public Long join(Request request){
        validateDuplicateRequest(request);//request의 user가 올린 requests에서 중복되는 exectime & address 확인
        checkExectime(request);//과거의 시간을 등록하려는지 확인
        requestRepository.save(request);
        return request.getId();
    }

    @Transactional
    public Long delete(Request request){
        if(Optional.ofNullable(requestRepository.findOne(request.getId())).isEmpty()){
            throw new RuntimeException("삭제하려는 Request가 존재하지 않습니다.");
        }
        requestRepository.delete(request);
        return request.getId();
    }

    public Optional<Request> findOne(Long id){
        return Optional.ofNullable(requestRepository.findOne(id));
    }

    public List<Request> findByUser(User user){
        return requestRepository.findByUser(user);
    }

    public List<Request> findByStatus(RequestStatus requestStatus){
        return requestRepository.findByStatus(requestStatus);
    }

    public List<Long> findKakaomapList(){
        return requestRepository.findByStatus(RequestStatus.REGISTER).stream().map(Request::getId).toList();
    }

    public List<Request> findAll(){
        return requestRepository.findAll();
    }

    //----내부로직----
    private void validateDuplicateRequest(Request request) {
        if(!this.findByUser(request.getUserR()).stream().filter(
                        r->r.getExectime().equals(request.getExectime())
                                &r.getAddress().equals(request.getAddress()))
                .toList().isEmpty()){
            throw new IllegalStateException("이미 존재하는 봉사요청입니다.");
        }
    }

    private void checkExectime(Request request) {
        if(request.getExectime().isBefore(LocalDateTime.now())){
            throw new IllegalStateException("수행시간이 현재시간보다 이전입니다.");
        }
    }
}
