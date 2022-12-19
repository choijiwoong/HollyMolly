package com.holly.molly;

import com.holly.molly.domain.*;
import com.holly.molly.service.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class IntegralTest {
    @Autowired UserService userService;
    @Autowired RequestService requestService;
    @Autowired AcceptService acceptService;
    @Autowired ReviewService reviewService;

    @Autowired AsyncService asyncService;

    @Autowired
    RequestCommentService requestCommentService;

    @AfterEach
    public void afterEach(){
        userService.clear();
        requestService.clear();
        acceptService.clear();
        reviewService.clear();
        requestCommentService.clear();
    }

    @Test
    public void fullLogic(){
        //given
        User user1=new User("홍길동", "honghonghong@gmail.com", "0000", "010-1234-5678", "950128-2038273");
        User user2=new User("홍승만", "hongmangirl@gmail.com", "1234", "010-5678-1234", "450128-2038273");
        userService.join(user1);
        userService.join(user2);

        Request request=new Request(user1, LocalDateTime.now().plusDays(1l), "서울시 서초구 방배동", "시력보조");
        requestService.join(request);

        RequestComment comment=new RequestComment(request, user2.getName(), "봉사는 정확히 어떤 활동인가요?");
        requestCommentService.join(comment);

        RequestComment comment1=new RequestComment(request, user1.getName(), "그냥 제 얘기 들어주시면 됩니다!");
        requestCommentService.join(comment1);

        Accept accept=new Accept(user2, request);
        acceptService.join(accept);

        request.setExectime(LocalDateTime.now().minusMinutes(1l));
        //리뷰후 status변경 필요

        Review review=new Review("봉사후기", "좆같았어요");
        reviewService.join(review);

        Review review1=new Review("피봉사자가 욕을 많이하네요..", "전 열심히 했는데 자꾸 좆같았데요...");
        reviewService.join(review1);

        asyncService.join();

        //when (user<->request and user<->accept)
        //then
        assertEquals(user1.getRequests().size(), 1);
        assertEquals(user1.getAccepts().size(), 0);
        assertEquals(user2.getAccepts().size(),1);
        assertEquals(user2.getRequests().size(), 0);

        assertEquals(user1.getRequests().stream().findFirst().get().getId(), request.getId());
        assertEquals(user2.getAccepts().stream().findFirst().get().getId(), accept.getId());

        assertEquals(request.getUserR().getId(), user1.getId());
        assertEquals(accept.getUserA().getId(), user2.getId());

        //when (request<->accept)
        //then
        assertEquals(request.getAccept().getId(), accept.getId());
        assertEquals(request.getStatus(), RequestStatus.COMPLETE);
        assertEquals(accept.getStatus(), AcceptStatus.COMPLETE);
        assertEquals(accept.getRequest().getUserR().getPid(), user1.getPid());

        //when (request<->comment)
        //then
        assertEquals(request.getComments().size(), 2);
        assertEquals(requestCommentService.findByRequest(request).size(), 2);
        assertEquals(request.getComments().get(0).getName(), user2.getName());
        assertEquals(request.getComments().get(1).getName(), user1.getName());
        assertEquals(request.getComments().get(0).getContent(), comment.getContent());
        assertEquals(request.getComments().get(1).getContent(), comment1.getContent());
        assertEquals(comment1.getRequest().getAccept().getUserA().getPid(), user2.getPid());

        //when(review)
        //then
        assertEquals(reviewService.findAll().size(), 2);
    }
}
