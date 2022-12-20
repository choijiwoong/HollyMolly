package com.holly.molly.service;

import com.holly.molly.domain.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AcceptServiceTest {
    @Autowired AcceptService acceptService;
    @Autowired RequestService requestService;
    @Autowired UserService userService;
    @Autowired
    EntityManager em;

    @AfterEach
    public void afterEach(){
        em.flush();
    }

    @Test
    void findOne() {
        //given
        User user=new User("user1","user@gmail.com","1234","010-0000-0000","000000-0000000");
        userService.join(user);

        Request request=new Request(user, LocalDateTime.now().plusDays(1l), "서울시 서초구 방배동", "교육봉사");
        requestService.join(request);

        User user2=new User("user2","user2@gmail.com","1234","010-0010-0000","100000-0000000");
        userService.join(user2);

        Accept accept=new Accept(user2, request);
        acceptService.join(accept);

        //when
        assertEquals(acceptService.findOne(accept.getId()).get().getId(), accept.getId());
    }

    @Test
    void findByUser() {
        //given
        User user=new User("user1","user@gmail.com","1234","010-0000-0000","000000-0000000");
        userService.join(user);

        Request request=new Request(user, LocalDateTime.now().plusDays(1l), "서울시 서초구 방배동", "교육봉사");
        requestService.join(request);

        User user2=new User("user2","user2@gmail.com","1234","010-0010-0000","100000-0000000");
        userService.join(user2);

        Accept accept=new Accept(user2, request);
        acceptService.join(accept);

        //when
        assertEquals(acceptService.findByUser(user2).size(), 1l);
        assertEquals(acceptService.findByUser(user2).stream().findFirst().get().getId(), accept.getId());
    }

    @Test
    void findByStatus() {
        //given
        User user=new User("user1","user@gmail.com","1234","010-0000-0000","000000-0000000");
        userService.join(user);

        Request request=new Request(user, LocalDateTime.now().plusDays(1l), "서울시 서초구 방배동", "교육봉사");
        requestService.join(request);

        User user2=new User("user2","user2@gmail.com","1234","010-0010-0000","100000-0000000");
        userService.join(user2);

        Accept accept=new Accept(user2, request);
        acceptService.join(accept);

        //when
        assertEquals(acceptService.findByStatus(AcceptStatus.REGISTER).size(), 1l);
        assertEquals(acceptService.findByStatus(AcceptStatus.REGISTER).stream().findFirst().get().getId(), accept.getId());
    }

    @Test
    void findAll() {
        //given
        User user=new User("user1","user@gmail.com","1234","010-0000-0000","000000-0000000");
        userService.join(user);

        Request request=new Request(user, LocalDateTime.now().plusDays(1l), "서울시 서초구 방배동", "교육봉사");
        requestService.join(request);

        User user2=new User("user2","user2@gmail.com","1234","010-0010-0000","100000-0000000");
        userService.join(user2);

        Accept accept=new Accept(user2, request);
        acceptService.join(accept);

        //when
        assertEquals(acceptService.findAll().size(), 1l);
        assertEquals(acceptService.findAll().stream().findFirst().get().getId(), accept.getId());
    }
}