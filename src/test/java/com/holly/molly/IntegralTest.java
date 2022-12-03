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
        User user1=new User();
        user1.setName("홍길동");
        user1.setPhone("010-1234-5678");
        user1.setEmail("honghonghong@gmail.com");
        user1.setPid("950128-2038273");
        user1.setPassword("0000");
        user1.setBirth("1995.01.28");

        User user2=new User();
        user2.setName("홍승만");
        user2.setPhone("010-5678-1234");
        user2.setEmail("hongmangirl@gmail.com");
        user2.setPid("450128-2038273");
        user2.setPassword("1234");
        user2.setBirth("1945.01.28");

        userService.join(user1);
        userService.join(user2);

        Request request=new Request();
        request.setStatus(RequestStatus.REGISTER);
        request.setContent("시력보조");
        request.setExectime(LocalDateTime.of(2022, 12, 25, 13,0));
        request.setReqtime(LocalDateTime.now());
        request.setUserR(user1);

        requestService.join(request);

        RequestComment comment=new RequestComment();
        comment.setName(user2.getName());
        comment.setContent("봉사는 정확히 어떤 활동인가요?");
        comment.setPosttime(LocalDateTime.now());
        comment.setRequest(request);

        requestCommentService.join(comment);

        RequestComment comment1=new RequestComment();
        comment1.setName(user1.getName());
        comment1.setContent("그냥 제 얘기 들어주시면 됩니다!");
        comment1.setPosttime(LocalDateTime.now());
        comment1.setRequest(request);

        requestCommentService.join(comment1);

        Accept accept=new Accept();
        accept.setUserA(user2);
        accept.setAcctime(LocalDateTime.now());
        accept.setStatus(AcceptStatus.REGISTER);
        accept.setRequest(request);

        acceptService.join(accept);

        Accept accept2=acceptService.findOne(accept.getId());
        accept2.setStatus(AcceptStatus.COMPLETE);

        Review review=new Review();
        review.setTitle("봉사후기");
        review.setContent("좆같았어요");

        reviewService.join(review);

        Review review1=new Review();
        review1.setTitle("피봉사자가 욕을 많이하네요..");
        review1.setContent("전 열심히 했는데 자꾸 좆같았데요...");

        reviewService.join(review1);

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
