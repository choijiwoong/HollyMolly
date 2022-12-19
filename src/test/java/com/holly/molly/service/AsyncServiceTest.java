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
        User user=new User("user1","user@gmail.com","1234","010-0000-0000","000000-0000000");
        userService.join(user);

        Request request=new Request(user, LocalDateTime.now().plusDays(1l), "서울시 서초구 방배동", "교육봉사");
        requestService.join(request);

        request.setExectime(LocalDateTime.now().minusMinutes(1l));
        //when(exectime is before now)
        asyncService.checkCancelCondition(requestService.findAll());

        //then
        assertEquals(request.getStatus(), RequestStatus.CANCEL);
    }

    @Test
    void checkCancleStatus() {//Accept는 Register인데 연결된 Request가 cancel인 경우 accept도 cancel처리
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
        request.changeStatus(RequestStatus.CANCEL);
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