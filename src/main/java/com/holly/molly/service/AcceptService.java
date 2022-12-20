package com.holly.molly.service;

import com.holly.molly.domain.*;
import com.holly.molly.repository.AcceptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AcceptService {
    private final AcceptRepository acceptRepository;
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
}