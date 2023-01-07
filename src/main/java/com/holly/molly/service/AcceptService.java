package com.holly.molly.service;

import com.holly.molly.domain.*;
import com.holly.molly.repository.AcceptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AcceptService {
    private final AcceptRepository acceptRepository;
    private final UserService userService;
    private final RequestService requestService;
    //*****************DB******************
    @Transactional
    public Long join(Accept accept){//accept는 특성상 duplicate가 발생할 수 없음.
        acceptRepository.save(accept);
        return accept.getId();
    }

    @Transactional
    public Long delete(Accept accept){
        if(Optional.ofNullable(acceptRepository.findOne(accept.getId())).isEmpty()){
            throw new RuntimeException("삭제하려는 Accept가 존재하지 않습니다.");
        }
        acceptRepository.delete(accept);
        return accept.getId();
    }

    public Optional<Accept> findOne(Long id){
        return Optional.ofNullable(acceptRepository.findOne(id));
    }

    public List<Accept> findByUser(User user){
        return acceptRepository.findByUser(user);
    }

    public List<Accept> findByStatus(AcceptStatus acceptStatus){
        return acceptRepository.findByStatus(acceptStatus);
    }

    public List<Accept> findAll(){
        return acceptRepository.findAll();
    }

    //**************서비스로직******************
    @Transactional
    public void SrvCreateAccept(Long requestId, Cookie cookie) {
        Optional<Request> request;
        if((request=requestService.findOne(requestId)).isEmpty())
            throw new RuntimeException("수락하고자 하는 봉사요청에 대한 정보가 없습니다!");

        this.join(new Accept(userService.parseUserCookie(cookie), request.get()));
    }
}