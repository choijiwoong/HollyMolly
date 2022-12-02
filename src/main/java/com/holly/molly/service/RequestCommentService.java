package com.holly.molly.service;

import com.holly.molly.domain.Request;
import com.holly.molly.domain.RequestComment;
import com.holly.molly.repository.RequestCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RequestCommentService {
    private final RequestCommentRepository requestCommentRepository;

    @Transactional
    public void join(RequestComment requestComment){ requestCommentRepository.save(requestComment); }

    @Transactional
    public void delete(RequestComment requestComment){ requestCommentRepository.delete(requestComment); }

    @Transactional
    public void clear(){ requestCommentRepository.clear(); }

    public RequestComment findOne(Long id){ return requestCommentRepository.findOne(id); }

    public List<RequestComment> findByRequest(Request request){ return requestCommentRepository.findByRequest(request); }

    public List<RequestComment> findAll(){ return requestCommentRepository.findAll(); }
}
