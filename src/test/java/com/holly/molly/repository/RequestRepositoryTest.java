package com.holly.molly.repository;

import com.holly.molly.domain.Request;
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
class RequestRepositoryTest {
    @Autowired RequestRepository requestRepository;
    @Autowired UserRepository userRepository;

    @AfterEach
    public void afterEach(){
        requestRepository.clear();
        userRepository.clear();
    }

    @Test
    void findOne() {
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

        //when
        Request request2=requestRepository.findOne(request.getId());

        //then
        assertEquals(request2.getUserR().getId(), user.getId());
    }

    @Test
    void findByUser() {
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

        //when
        List<Request> requests=requestRepository.findByUser(user);

        //then
        assertEquals(requests.stream().findFirst().get().getId(), request.getId());
    }

    @Test
    void findByStatus() {
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

        //when
        List<Request> requests=requestRepository.findByStatus(RequestStatus.REGISTER);

        //then
        assertEquals(requests.stream().findFirst().get().getId(), request.getId());
    }

    @Test
    void findAll() {
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

        //when
        List<Request> requests=requestRepository.findAll();

        //then
        assertEquals(requests.size(), 1l);
        assertEquals(requests.stream().findFirst().get().getId(), request.getId());
    }
}