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

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RequestCommentRepositoryTest {
    @Autowired
    RequestCommentRepository requestCommentRepository;
    @Autowired RequestRepository requestRepository;
    @Autowired UserRepository userRepository;
    @Autowired
    EntityManager em;

    @AfterEach
    public void afterEach(){ em.clear(); }

    @Test
    void findOne() {
        //given
        User user=new User("홍길동","hongil@gmail.com","1234","010-0000-0000","000000-0000000");
        userRepository.save(user);

        Request request=new Request(user, LocalDateTime.now().plusDays(1l), "서울시 서촉수 방배동", "아동복지관 봉사활동");
        requestRepository.save(request);

        RequestComment requestComment =new RequestComment(request, "홍길동", "안녕하세요");
        requestCommentRepository.save(requestComment);

        //when
        RequestComment requestComment2 = requestCommentRepository.findOne(requestComment.getId());

        //then
        assertEquals(requestComment2.getId(), requestComment.getId());
    }

    @Test
    void findAll() {
        //given
        User user=new User("홍길동","hongil@gmail.com","1234","010-0000-0000","000000-0000000");
        userRepository.save(user);

        Request request=new Request(user, LocalDateTime.now().plusDays(1l), "서울시 서촉수 방배동", "아동복지관 봉사활동");
        requestRepository.save(request);

        RequestComment requestComment =new RequestComment(request, "홍길동", "안녕하세요");
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
        User user=new User("홍길동","hongil@gmail.com","1234","010-0000-0000","000000-0000000");
        userRepository.save(user);

        Request request=new Request(user, LocalDateTime.now().plusDays(1l), "서울시 서촉수 방배동", "아동복지관 봉사활동");
        requestRepository.save(request);

        RequestComment requestComment =new RequestComment(request, "홍길동", "안녕하세요");
        requestCommentRepository.save(requestComment);

        //when
        List<RequestComment> requestComments=requestCommentRepository.findByRequest(request);

        //then
        assertEquals(requestComments.size(), 1);
        assertEquals(requestComments.stream().findFirst().get().getRequest().getId(), request.getId());
    }
}