package com.holly.molly.service;

import com.holly.molly.DTO.CommentDTO;
import com.holly.molly.domain.Request;
import com.holly.molly.domain.RequestComment;
import com.holly.molly.domain.User;
import com.holly.molly.repository.RequestCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RequestCommentService {
    private final RequestCommentRepository requestCommentRepository;
    private final RequestService requestService;
    private final UserService userService;

    //***********DB***********
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

    //***********서비스로직***********
    @Transactional
    public Request SrvCreateRequestComment(Cookie cookie, CommentDTO commentDTO) {
        Long requestId = commentDTO.getHid();
        User user = userService.parseUserCookie(cookie);

        Optional<Request> request;
        if((request=requestService.findOne(requestId)).isEmpty())
            throw new RuntimeException("댓글등록중 필요한 봉사요청 정보를 찾지 못했습니다.");

        RequestComment comment = new RequestComment(request.get(), user.getName(), commentDTO.getContent());
        this.join(comment);

        return request.get();
    }
}
