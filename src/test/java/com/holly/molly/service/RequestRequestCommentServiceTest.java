package com.holly.molly.service;

import com.holly.molly.DTO.CommentDTO;
import com.holly.molly.DTO.RequestDTO;
import com.holly.molly.domain.Request;
import com.holly.molly.domain.RequestComment;
import com.holly.molly.domain.RequestStatus;
import com.holly.molly.domain.User;
import com.holly.molly.repository.RequestRepository;
import com.holly.molly.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.servlet.http.Cookie;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class RequestRequestCommentServiceTest {
    @Autowired
    RequestCommentService requestCommentService;
    @Autowired
    RequestService requestService;
    @Autowired
    UserService userService;
    @Autowired
    EntityManager em;

    @AfterEach
    public void afterEach(){ em.clear(); }

    @Test
    void findOne() {
        //given
        User user=new User("홍길동","hongil@gmail.com","1234","010-0000-0000","000000-0000000");
        userService.join(user);

        Request request=new Request(user, LocalDateTime.now().plusDays(1l), "서울시 서촉수 방배동", "아동복지관 봉사활동", "37.566826", "126.9786567");
        requestService.join(request);

        RequestComment requestComment =new RequestComment(request, "홍길동", "안녕하세요");
        requestCommentService.join(requestComment);

        //when
        RequestComment requestComment2 = requestCommentService.findOne(requestComment.getId());

        //then
        assertEquals(requestComment2.getId(), requestComment.getId());
    }

    @Test
    void findAll() {
        //given
        User user=new User("홍길동","hongil@gmail.com","1234","010-0000-0000","000000-0000000");
        userService.join(user);

        Request request=new Request(user, LocalDateTime.now().plusDays(1l), "서울시 서촉수 방배동", "아동복지관 봉사활동", "37.566826", "126.9786567");
        requestService.join(request);

        RequestComment requestComment =new RequestComment(request, "홍길동", "안녕하세요");
        requestCommentService.join(requestComment);

        //when
        List<RequestComment> requestComments = requestCommentService.findAll();

        //then
        assertEquals(requestComments.size(),1);
        assertEquals(requestComments.stream().findFirst().get().getId(), requestComment.getId());
    }

    @Test
    void findByRequest(){
        //given
        User user=new User("홍길동","hongil@gmail.com","1234","010-0000-0000","000000-0000000");
        userService.join(user);

        Request request=new Request(user, LocalDateTime.now().plusDays(1l), "서울시 서촉수 방배동", "아동복지관 봉사활동", "37.566826", "126.9786567");
        requestService.join(request);

        RequestComment requestComment =new RequestComment(request, "홍길동", "안녕하세요");
        requestCommentService.join(requestComment);

        //when
        List<RequestComment> requestComments=requestCommentService.findByRequest(request);

        //then
        assertEquals(requestComments.size(), 1);
        assertEquals(requestComments.stream().findFirst().get().getRequest().getId(), request.getId());
    }

    @Test
    public void assosiationTest(){
        //given
        User user=new User("홍길동","hongil@gmail.com","1234","010-0000-0000","000000-0000000");
        userService.join(user);

        Request request=new Request(user, LocalDateTime.now().plusDays(1l), "서울시 서촉수 방배동", "아동복지관 봉사활동", "37.566826", "126.9786567");
        requestService.join(request);

        RequestComment requestComment =new RequestComment(request, "홍길동", "안녕하세요");
        requestCommentService.join(requestComment);

        RequestComment requestComment2 =new RequestComment(request, "홍길동", "안녕하세요");
        requestCommentService.join(requestComment);

        //when
        Request request_=user.getRequests().stream().findFirst().get();
        List<RequestComment> comments=request_.getComments();

        //then
        assertEquals(comments.size(), 2);
        assertEquals(comments.get(0).getContent(), "안녕하세요");
        assertEquals(comments.get(1).getContent(), "안녕하세요");
    }

    @Test
    void SrvCreateRequestComment(){
        //given
        User user1=new User("user1","user@gmail.com","1234","010-0000-0000","000000-0000000");
        userService.join(user1);

        Request request=new Request(user1, "2025.01.01.00.00", "서울시 서초구 방배동", "노인봉사", "37.566826", "126.9786567");
        requestService.join(request);

        User user2=new User("user2","user2@gmail.com","1234","010-0010-0000","100000-0000000");
        userService.join(user2);

        Cookie cookie=new Cookie("userId", String.valueOf(user2.getId()));
        CommentDTO commentDTO=new CommentDTO("너무 좋아요", String.valueOf(request.getId()));//자바스크립트이용 자동채워넣기때문에.

        //when
        Request request2=requestCommentService.SrvCreateRequestComment(cookie, commentDTO);

        //then
        assertEquals(requestCommentService.findAll().size(),1);
        assertEquals(requestCommentService.findAll().stream().findFirst().get().getRequest().getId(), request.getId());
        assertEquals(request2.getId(), request.getId());
    }
}