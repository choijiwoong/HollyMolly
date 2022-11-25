package com.holly.molly.service;

import com.holly.molly.domain.*;
import com.holly.molly.repository.AcceptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AcceptService {
    private final AcceptRepository acceptRepository;
    @Transactional//필요시에만 readOnly=false
    public Long join(Accept accept){
        acceptRepository.save(accept);
        return accept.getId();
    }

    public Accept findOne(Long id){
        return acceptRepository.findOne(id);
    }

    public List<Accept> findByUser(User user){
        return acceptRepository.findByUser(user);
    }

    public List<Accept> findByStatus(AcceptStatus acceptStatus){
        return acceptRepository.findByStatus(acceptStatus);
    }

    public List<Accept> findByReqtime(LocalDateTime localDateTime){
        return acceptRepository.findByAcctime(localDateTime);
    }

    public List<Accept> findAll(){
        return acceptRepository.findAll();
    }
}