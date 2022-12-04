package com.holly.molly.service;

import com.holly.molly.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AsyncServiceTest {
    @Autowired AsyncService asyncService;
    @Autowired UserService userService;
    @Autowired RequestService requestService;
    @Autowired AcceptService acceptService;

    @Test
    void checkCancelCondition() {//Request의 exectime이 지나는 경우 자동 cancel처리
        //given
        User user=new User();
        user.setBirth("0000.00.00");
        user.setPassword("1234");
        user.setPid("000000-0000000");
        user.setEmail("user@gmail.com");
        user.setPhone("000-0000-0000");
        user.setName("user1");

        userService.join(user);

        LocalDateTime exectime=LocalDateTime.now();

        Request request=new Request();
        request.setAddress("서울시 서초구 방배동");
        request.setExectime(exectime);
        request.setReqtime(LocalDateTime.now());
        request.setContent("교육봉사");
        request.setStatus(RequestStatus.REGISTER);
        request.setUserR(user);

        requestService.join(request);

        //when(exectime is before now)
        asyncService.checkCancelCondition(requestService.findAll());

        //then
        assertEquals(request.getStatus(), RequestStatus.CANCEL);
    }

    @Test
    void checkCancleStatus() {//Accept는 Register인데 연결된 Request가 cancel인 경우 accept도 cancel처리
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
        request.setStatus(RequestStatus.CANCEL);
        asyncService.checkCancleStatus(acceptService.findAll());

        //then
        assertEquals(accept.getStatus(), AcceptStatus.CANCEL);
    }

    @Test
    void checkNoticeMailTiming() {//봉사 하루전 알림메일 전송

    }

    @Test
    void checkComplete() {//exectime이 지나면 complete처리

    }

    @Test
    void checkReviewTiming() {//exectime의 day가 지나면 리뷰url이 담긴 메일 전송
    }
}