package com.holly.molly.service;

import com.holly.molly.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

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

        Request request=new Request(user, LocalDateTime.now().plusDays(1l), "서울시 서초구 방배동", "교육봉사", "37.566826", "126.9786567");
        requestService.join(request);

        request.setExectime(LocalDateTime.now().minusMinutes(1l));
        //when(exectime is before now)
        asyncService.checkCancelCondition(requestService.findOne(request.getId()).get());

        //then
        assertEquals(request.getStatus(), RequestStatus.CANCEL);
    }

    @Test
    void checkCancleStatus() {//Accept는 Register인데 연결된 Request가 cancel인 경우 accept도 cancel처리
        //given
        User user=new User("user1","user@gmail.com","1234","010-0000-0000","000000-0000000");
        userService.join(user);

        Request request=new Request(user, LocalDateTime.now().plusDays(1l), "서울시 서초구 방배동", "교육봉사", "37.566826", "126.9786567");
        requestService.join(request);

        User user2=new User("user2","user2@gmail.com","1234","010-0010-0000","100000-0000000");
        userService.join(user2);

        Accept accept=new Accept(user2, request);
        acceptService.join(accept);

        //when
        request.changeStatus(RequestStatus.CANCEL);
        asyncService.checkCancleStatus(acceptService.findOne(accept.getId()).get());

        //then
        assertEquals(accept.getStatus(), AcceptStatus.CANCEL);
    }

    @Test
    void checkNoticeMailTiming() {//봉사 하루전 알림메일 전송
        //given
        List<Accept> acceptList= Collections.emptyList();

        //when
        asyncService.checkNoticeMailTiming(acceptList);

        //then 확인할 수락리스트가 없기에(조건을 만족하는 accept가 없기에) 아무메일도 발송x
    }

    @Test
    void checkRunning(){
        //given
        //when
        //then
    }

    @Test
    void checkComplete() {//exectime이 지나면 complete처리
        //given
        User user=new User("user1","user@gmail.com","1234","010-0000-0000","000000-0000000");
        userService.join(user);

        Request request=new Request(user, LocalDateTime.now().plusDays(1l), "서울시 서초구 방배동", "교육봉사", "37.566826", "126.9786567");
        requestService.join(request);

        User user2=new User("user2","user2@gmail.com","1234","010-0010-0000","100000-0000000");
        userService.join(user2);

        Accept accept=new Accept(user2, request);
        acceptService.join(accept);

        //when
        request.setExectime(LocalDateTime.now().minusHours(3l));//시간이 한참지났는데
        request.changeStatus(RequestStatus.RUNNING);//아직 봉사중인 경우
        accept.changeStatus(AcceptStatus.RUNNING);
        System.out.println("[DEBUG]1 "+request.getStatus()+", "+accept.getStatus());
        asyncService.checkComplete(accept);
        System.out.println("[DEBUG]4 "+request.getStatus()+", "+accept.getStatus());

        //then
        assertEquals(request.getStatus(), RequestStatus.COMPLETE);
        assertEquals(accept.getStatus(), AcceptStatus.COMPLETE);
    }

    @Test
    void checkReviewTiming() {//exectime의 day가 지나면 리뷰url이 담긴 메일 전송
        //given
        User user=new User("user1","user@gmail.com","1234","010-0000-0000","000000-0000000");
        userService.join(user);

        Request request=new Request(user, LocalDateTime.now().plusDays(1l), "서울시 서초구 방배동", "교육봉사", "37.566826", "126.9786567");
        requestService.join(request);

        User user2=new User("user2","user2@gmail.com","1234","010-0010-0000","100000-0000000");
        userService.join(user2);

        Accept accept=new Accept(user2, request);
        acceptService.join(accept);

        //when
        asyncService.checkReviewTiming(accept);

        //then 완료 상태가 아니기에 아무 메일도 보내지 않는다
    }

    @Test
    void emergency(){
        //given
        User user=new User("user1","_@gmail.com","1234","010-0000-0000","000000-0000000");
        userService.join(user);

        Request request=new Request(user, LocalDateTime.now().plusMinutes(1l), "*********테스트중*********", "교육봉사", "37.566826", "126.9786567");
        requestService.join(request);

        User user2=new User("user2","__@gmail.com","1234","010-0010-0000","100000-0000000");
        userService.join(user2);

        Accept accept=new Accept(user2, request);
        acceptService.join(accept);

        //when_이미 시작한 경우에
        request.setExectime(LocalDateTime.now().minusMinutes(1l));
        request.changeStatus(RequestStatus.RUNNING);
        asyncService.emergency(request.getId());

        //then
        assertEquals(request.getStatus(), RequestStatus.EMERGENCY);
    }
}