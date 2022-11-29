package com.holly.molly.service;

import com.holly.molly.domain.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AcceptServiceTest {
    @Autowired AcceptService acceptService;
    @Autowired RequestService requestService;
    @Autowired UserService userService;

    @AfterEach
    public void afterEach(){
        acceptService.clear();
    }

    @Test
    void findOne() {
        //given
        User user=new User();
        user.setBirth("0000.00.00");
        user.setPassword("1234");
        user.setPid("000000-0000000");
        user.setEmail("user@gmail.com");
        user.setPhone("000-0000-0000");
        user.setName("user1");

        userService.join(user);

        LocalDateTime exectime=LocalDateTime.now().plusDays(1l);

        Request request=new Request();
        request.setAddress("서울시 서초구 방배동");
        request.setExectime(exectime);
        request.setReqtime(LocalDateTime.now());
        request.setContent("교육봉사");
        request.setStatus(RequestStatus.REGISTER);
        request.setUserR(user);

        requestService.join(request);

        User user2=new User();
        user2.setBirth("1000.00.00");
        user2.setPassword("1234");
        user2.setPid("100000-0000000");
        user2.setEmail("user2@gmail.com");
        user2.setPhone("010-0000-0000");
        user2.setName("user2");

        userService.join(user2);

        Accept accept=new Accept();
        accept.setUserA(user2);
        accept.setAcctime(LocalDateTime.now());
        accept.setStatus(AcceptStatus.REGISTER);
        accept.setRequest(request);

        acceptService.join(accept);

        //when
        assertEquals(acceptService.findOne(accept.getId()).getId(), accept.getId());
    }

    @Test
    void findByUser() {
        //given
        User user=new User();
        user.setBirth("0000.00.00");
        user.setPassword("1234");
        user.setPid("000000-0000000");
        user.setEmail("user@gmail.com");
        user.setPhone("000-0000-0000");
        user.setName("user1");

        userService.join(user);

        LocalDateTime exectime=LocalDateTime.now().plusDays(1l);

        Request request=new Request();
        request.setAddress("서울시 서초구 방배동");
        request.setExectime(exectime);
        request.setReqtime(LocalDateTime.now());
        request.setContent("교육봉사");
        request.setStatus(RequestStatus.REGISTER);
        request.setUserR(user);

        requestService.join(request);

        User user2=new User();
        user2.setBirth("1000.00.00");
        user2.setPassword("1234");
        user2.setPid("100000-0000000");
        user2.setEmail("user2@gmail.com");
        user2.setPhone("010-0000-0000");
        user2.setName("user2");

        userService.join(user2);

        Accept accept=new Accept();
        accept.setUserA(user2);
        accept.setAcctime(LocalDateTime.now());
        accept.setStatus(AcceptStatus.REGISTER);
        accept.setRequest(request);

        acceptService.join(accept);

        //when
        assertEquals(acceptService.findByUser(user2).size(), 1l);
        assertEquals(acceptService.findByUser(user2).stream().findFirst().get().getId(), accept.getId());
    }

    @Test
    void findByStatus() {
        //given
        User user=new User();
        user.setBirth("0000.00.00");
        user.setPassword("1234");
        user.setPid("000000-0000000");
        user.setEmail("user@gmail.com");
        user.setPhone("000-0000-0000");
        user.setName("user1");

        userService.join(user);

        LocalDateTime exectime=LocalDateTime.now().plusDays(1l);

        Request request=new Request();
        request.setAddress("서울시 서초구 방배동");
        request.setExectime(exectime);
        request.setReqtime(LocalDateTime.now());
        request.setContent("교육봉사");
        request.setStatus(RequestStatus.REGISTER);
        request.setUserR(user);

        requestService.join(request);

        User user2=new User();
        user2.setBirth("1000.00.00");
        user2.setPassword("1234");
        user2.setPid("100000-0000000");
        user2.setEmail("user2@gmail.com");
        user2.setPhone("010-0000-0000");
        user2.setName("user2");

        userService.join(user2);

        Accept accept=new Accept();
        accept.setUserA(user2);
        accept.setAcctime(LocalDateTime.now());
        accept.setStatus(AcceptStatus.REGISTER);
        accept.setRequest(request);

        acceptService.join(accept);

        //when
        assertEquals(acceptService.findByStatus(AcceptStatus.REGISTER).size(), 1l);
        assertEquals(acceptService.findByStatus(AcceptStatus.REGISTER).stream().findFirst().get().getId(), accept.getId());
    }

    @Test
    void findAll() {
        //given
        User user=new User();
        user.setBirth("0000.00.00");
        user.setPassword("1234");
        user.setPid("000000-0000000");
        user.setEmail("user@gmail.com");
        user.setPhone("000-0000-0000");
        user.setName("user1");

        userService.join(user);

        LocalDateTime exectime=LocalDateTime.now().plusDays(1l);

        Request request=new Request();
        request.setAddress("서울시 서초구 방배동");
        request.setExectime(exectime);
        request.setReqtime(LocalDateTime.now());
        request.setContent("교육봉사");
        request.setStatus(RequestStatus.REGISTER);
        request.setUserR(user);

        requestService.join(request);

        User user2=new User();
        user2.setBirth("1000.00.00");
        user2.setPassword("1234");
        user2.setPid("100000-0000000");
        user2.setEmail("user2@gmail.com");
        user2.setPhone("010-0000-0000");
        user2.setName("user2");

        userService.join(user2);

        Accept accept=new Accept();
        accept.setUserA(user2);
        accept.setAcctime(LocalDateTime.now());
        accept.setStatus(AcceptStatus.REGISTER);
        accept.setRequest(request);

        acceptService.join(accept);

        //when
        assertEquals(acceptService.findAll().size(), 1l);
        assertEquals(acceptService.findAll().stream().findFirst().get().getId(), accept.getId());
    }
}