package com.holly.molly;

import com.holly.molly.domain.*;
import com.holly.molly.service.AcceptService;
import com.holly.molly.service.RequestService;
import com.holly.molly.service.ReviewService;
import com.holly.molly.service.UserService;
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

    @AfterEach
    public void afterEach(){
        userService.clear();
        requestService.clear();
        acceptService.clear();
        reviewService.clear();
    }

    @Test
    public void fullLogic(){
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

        Accept accept=new Accept();
        accept.setUserA(user2);
        accept.setAcctime(LocalDateTime.now());
        accept.setStatus(AcceptStatus.REGISTER);
        accept.setRequest(request);

        acceptService.join(accept);

        Accept accept2=acceptService.findOne(accept.getId());
        accept2.setStatus(AcceptStatus.COMPLETE);

        assertEquals(request.getStatus(), RequestStatus.COMPLETE);
    }
}
