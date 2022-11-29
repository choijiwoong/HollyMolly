package com.holly.molly.service;

import com.holly.molly.domain.Request;
import com.holly.molly.domain.RequestStatus;
import com.holly.molly.domain.User;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RequestServiceTest {
    @Autowired RequestService requestService;
    @Autowired UserService userService;

    @AfterEach
    public void afterEach(){
        requestService.clear();
    }

    @Test
    void join() {
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

        //when
        Request request2=new Request();
        request2.setAddress("서울시 서초구 방배동");
        request2.setExectime(exectime);
        request2.setReqtime(LocalDateTime.now());
        request2.setContent("교육봉사");
        request2.setStatus(RequestStatus.REGISTER);
        request2.setUserR(user);

        //then
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> requestService.join(request2));//예외가 발생해야 한다.
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 봉사요청입니다.");
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

        //when
        assertEquals(requestService.findOne(request.getId()).getId(), request.getId());
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

        //when
        assertEquals(requestService.findByUser(user).size(), 1l);
        assertEquals(requestService.findByUser(user).stream().findFirst().get().getId(), request.getId());
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
        request.setStatus(RequestStatus.CANCEL);
        request.setUserR(user);

        requestService.join(request);

        //when
        assertEquals(requestService.findByStatus(RequestStatus.REGISTER).stream().findFirst(), Optional.empty());
        assertEquals(requestService.findByStatus(RequestStatus.CANCEL).size(), 1l);
        assertEquals(requestService.findByStatus(RequestStatus.CANCEL).stream().findFirst().get().getId(), request.getId());
    }

    @Test
    void findKakaomapList() {
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

        //when
        assertEquals(requestService.findKakaomapList().size(),1l);
        assertEquals(requestService.findKakaomapList().stream().findFirst().get(), request.getId());
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

        //when
        assertEquals(requestService.findAll().size(), 1l);
        assertEquals(requestService.findAll().stream().findFirst().get().getId(), request.getId());
    }
}