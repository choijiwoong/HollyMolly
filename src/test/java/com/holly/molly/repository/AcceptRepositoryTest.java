package com.holly.molly.repository;

import com.holly.molly.domain.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AcceptRepositoryTest {
    @Autowired AcceptRepository acceptRepository;
    @Autowired UserRepository userRepository;

    @Autowired RequestRepository requestRepository;
    @Autowired
    EntityManager em;

    @AfterEach
    public void afterEach(){
        em.clear();
    }

    @Test
    void findOne() {
        //given
        User user=new User("홍길동","hongil@gmail.com","1234","010-0000-0000","000000-0000000");
        userRepository.save(user);

        Request request=new Request(user, LocalDateTime.now().plusDays(1l), "서울시 서촉수 방배동", "아동복지관 봉사활동", "37.566826", "126.9786567", "1");
        requestRepository.save(request);

        User user2=new User("수바기","water@gmail.com","1234","010-1111-1111","000000-1000000");
        userRepository.save(user2);

        Accept accept=new Accept(user2, request);
        acceptRepository.save(accept);

        //when
        Accept accept2=acceptRepository.findOne(accept.getId());

        //then
        assertEquals(accept2.getRequest().getId(), accept.getRequest().getId());//request ID비교
        assertEquals(accept2.getUserA().getId(), user2.getId());//UserA ID비교
        assertEquals(accept2.getRequest().getUserR().getId(), user.getId());//UserB ID비교
    }

    @Test
    void findByStatus() {
        //given
        User user=new User("홍길동","hongil@gmail.com","1234","010-0000-0000","000000-0000000");
        userRepository.save(user);

        Request request=new Request(user, LocalDateTime.now().plusDays(1l), "서울시 서촉수 방배동", "아동복지관 봉사활동", "37.566826", "126.9786567", "1");
        requestRepository.save(request);

        User user2=new User("수바기","water@gmail.com","1234","010-1111-1111","000000-1000000");
        userRepository.save(user2);

        Accept accept=new Accept(user2, request);
        acceptRepository.save(accept);

        //when
        List<Accept> accepts=acceptRepository.findByStatus(AcceptStatus.REGISTER);

        //then
        assertEquals(accepts.stream().findFirst().get().getId(), accept.getId());
    }

    @Test
    void findByUser() {
        //given
        User user=new User("홍길동","hongil@gmail.com","1234","010-0000-0000","000000-0000000");
        userRepository.save(user);

        Request request=new Request(user, LocalDateTime.now().plusDays(1l), "서울시 서촉수 방배동", "아동복지관 봉사활동", "37.566826", "126.9786567", "1");
        requestRepository.save(request);

        User user2=new User("수바기","water@gmail.com","1234","010-1111-1111","000000-1000000");
        userRepository.save(user2);

        Accept accept=new Accept(user2, request);
        acceptRepository.save(accept);

        //when
        List<Accept> accepts=acceptRepository.findByUser(user2);

        //then
        assertEquals(accepts.stream().findFirst().get().getId(), accept.getId());
    }

    @Test
    void findAll() {
        //given
        User user=new User("홍길동","hongil@gmail.com","1234","010-0000-0000","000000-0000000");
        userRepository.save(user);

        Request request=new Request(user, LocalDateTime.now().plusDays(1l), "서울시 서촉수 방배동", "아동복지관 봉사활동", "37.566826", "126.9786567", "1");
        requestRepository.save(request);

        User user2=new User("수바기","water@gmail.com","1234","010-1111-1111","000000-1000000");
        userRepository.save(user2);

        Accept accept=new Accept(user2, request);
        acceptRepository.save(accept);

        //when
        List<Accept> accepts=acceptRepository.findAll();

        //then
        assertEquals(accepts.size(), 1l);
        assertEquals(accepts.stream().findFirst().get().getId(), accept.getId());
    }
}