package com.holly.molly.repository;

import com.holly.molly.domain.Request;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RequestRepositoryTest {
    @Autowired RequestRepository requestRepository;
    @Autowired UserRepository userRepository;
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

        //when
        Request request2=requestRepository.findOne(request.getId());

        //then
        assertEquals(request2.getUserR().getId(), user.getId());
    }

    @Test
    void findByUser() {
        //given
        User user=new User("홍길동","hongil@gmail.com","1234","010-0000-0000","000000-0000000");
        userRepository.save(user);

        Request request=new Request(user, LocalDateTime.now().plusDays(1l), "서울시 서촉수 방배동", "아동복지관 봉사활동", "37.566826", "126.9786567", "1");
        requestRepository.save(request);

        //when
        List<Request> requests=requestRepository.findByUser(user);

        //then
        assertEquals(requests.stream().findFirst().get().getId(), request.getId());
    }

    @Test
    void findByStatus() {
        //given
        User user=new User("홍길동","hongil@gmail.com","1234","010-0000-0000","000000-0000000");
        userRepository.save(user);

        Request request=new Request(user, LocalDateTime.now().plusDays(1l), "서울시 서촉수 방배동", "아동복지관 봉사활동", "37.566826", "126.9786567", "1");
        requestRepository.save(request);

        //when
        List<Request> requests=requestRepository.findByStatus(RequestStatus.REGISTER);

        //then
        assertEquals(requests.stream().findFirst().get().getId(), request.getId());
    }

    @Test
    void findAll() {
        //given
        User user=new User("홍길동","hongil@gmail.com","1234","010-0000-0000","000000-0000000");
        userRepository.save(user);

        Request request=new Request(user, LocalDateTime.now().plusDays(1l), "서울시 서촉수 방배동", "아동복지관 봉사활동", "37.566826", "126.9786567", "1");
        requestRepository.save(request);

        //when
        List<Request> requests=requestRepository.findAll();

        //then
        assertEquals(requests.size(), 1l);
        assertEquals(requests.stream().findFirst().get().getId(), request.getId());
    }
}