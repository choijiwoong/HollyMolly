package com.holly.molly.repository;

import com.holly.molly.domain.Request;
import com.holly.molly.domain.RequestComment;
import com.holly.molly.domain.RequestStatus;
import com.holly.molly.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RequestRequestCommentRepositoryTest {
    @Autowired
    RequestCommentRepository requestCommentRepository;
    @Autowired RequestRepository requestRepository;
    @Autowired UserRepository userRepository;

    @AfterEach
    public void afterEach(){ requestCommentRepository.clear(); }

    @Test
    void findOne() {
        //given
        RequestComment requestComment =new RequestComment();
        requestComment.setName("홍길동");
        requestComment.setPosttime(LocalDateTime.now());
        requestComment.setContent("안녕하세요");

        requestCommentRepository.save(requestComment);

        //when
        RequestComment requestComment2 = requestCommentRepository.findOne(requestComment.getId());

        //then
        assertEquals(requestComment2.getId(), requestComment.getId());
        assertEquals(requestComment2.getPosttime(), requestComment.getPosttime());
    }

    @Test
    void findAll() {
        //given
        RequestComment requestComment =new RequestComment();
        requestComment.setName("홍길동");
        requestComment.setPosttime(LocalDateTime.now());
        requestComment.setContent("안녕하세요");

        requestCommentRepository.save(requestComment);

        //when
        List<RequestComment> requestComments = requestCommentRepository.findAll();

        //then
        assertEquals(requestComments.size(),1);
        assertEquals(requestComments.stream().findFirst().get().getId(), requestComment.getId());
    }

    @Test
    void findByRequest(){
        //given
        User user=new User();
        user.setName("홍길동");
        user.setPhone("010-0000-0000");
        user.setEmail("hongil@gmail.com");
        user.setPid("000000-0000000");
        user.setPassword("1234");
        user.setBirth("0000.00.00");

        userRepository.save(user);

        Request request=new Request();
        request.setUserR(user);
        request.setStatus(RequestStatus.REGISTER);
        request.setExectime(LocalDateTime.now().plusDays(1l));
        request.setContent("아동복지관 봉사활동");
        request.setReqtime(LocalDateTime.now());
        request.setAddress("서울시 서초구 방배동");

        requestRepository.save(request);

        RequestComment requestComment =new RequestComment();
        requestComment.setName("홍길동");
        requestComment.setPosttime(LocalDateTime.now());
        requestComment.setContent("안녕하세요");
        requestComment.setRequest(request);

        requestCommentRepository.save(requestComment);

        //when
        List<RequestComment> requestComments=requestCommentRepository.findByRequest(request);

        //then
        assertEquals(requestComments.size(), 1);
        assertEquals(requestComments.stream().findFirst().get().getRequest().getId(), request.getId());
    }
}