package com.holly.molly.service;

import com.holly.molly.domain.Request;
import com.holly.molly.domain.RequestComment;
import com.holly.molly.repository.RequestCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RequestCommentService {
    private final RequestCommentRepository requestCommentRepository;

    @Transactional
    public void join(RequestComment requestComment){ requestCommentRepository.save(requestComment); }

    @Transactional
    public void delete(RequestComment requestComment){
        if(Optional.ofNullable(requestCommentRepository.findOne(requestComment.getId())).isEmpty()){
            throw new RuntimeException("삭제하려는 Accept가 존재하지 않습니다.");
        }
        requestCommentRepository.delete(requestComment);
    }

    public RequestComment findOne(Long id){ return requestCommentRepository.findOne(id); }

    public List<RequestComment> findByRequest(Request request){ return requestCommentRepository.findByRequest(request); }

    public List<RequestComment> findAll(){ return requestCommentRepository.findAll(); }
}
