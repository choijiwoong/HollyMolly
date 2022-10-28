package com.holly.molly.service;

import com.holly.molly.domain.*;
import com.holly.molly.repository.AcceptRepository;
import com.holly.molly.repository.VolunRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VolunService {
    private final VolunRepository volunRepository;
    @Transactional//필요시에만 readOnly=false
    public Long join(Volun volun){
        volunRepository.save(volun);
        return volun.getId();
    }

    public Volun findOne(Long id){
        return volunRepository.findOne(id);
    }

    public List<Volun> findByRequest(Request request){
        return volunRepository.findByRequest(request);
    }

    public List<Volun> findByAccept(Accept accept){
        return volunRepository.findByAccept(accept);
    }

    public List<Volun> findByStatus(VolunStatus volunStatus){
        return volunRepository.findByStatus(volunStatus);
    }

    public List<Volun> findByExectime(LocalDateTime localDateTime){
        return volunRepository.findByExectime(localDateTime);
    }

    public List<Volun> findAll(){
        return volunRepository.findAll();
    }
}