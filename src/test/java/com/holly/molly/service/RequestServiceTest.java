package com.holly.molly.service;

import com.holly.molly.DTO.LocationDTO;
import com.holly.molly.DTO.NearRequestListElementDTO;
import com.holly.molly.DTO.RequestDTO;
import com.holly.molly.domain.Accept;
import com.holly.molly.domain.Request;
import com.holly.molly.domain.RequestStatus;
import com.holly.molly.domain.User;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.servlet.http.Cookie;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RequestServiceTest {
    @Autowired RequestService requestService;
    @Autowired UserService userService;
    @Autowired
    EntityManager em;

    @AfterEach
    public void afterEach(){
        em.clear();
    }

    @Test
    void join() {
        //given
        User user=new User("user1","user@gmail.com","1234","010-0000-0000","000000-0000000");
        userService.join(user);

        LocalDateTime time=LocalDateTime.now().plusDays(1l);

        Request request=new Request(user, time, "서울시 서초구 방배동", "교육봉사", "37.566826", "126.9786567", "1");
        requestService.join(request);

        //when
        Request request2=new Request(user, time, "서울시 서초구 방배동", "교육봉사", "37.566826", "126.9786567", "1");

        //then
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> requestService.join(request2));//예외가 발생해야 한다.
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 봉사요청입니다.");
    }

    @Test
    void findOne() {
        //given
        User user=new User("user1","user@gmail.com","1234","010-0000-0000","000000-0000000");
        userService.join(user);

        Request request=new Request(user, LocalDateTime.now().plusDays(1l), "서울시 서초구 방배동", "교육봉사", "37.566826", "126.9786567", "1");
        requestService.join(request);

        //when
        assertEquals(requestService.findOne(request.getId()).get().getId(), request.getId());
    }

    @Test
    void findByUser() {
        //given
        User user=new User("user1","user@gmail.com","1234","010-0000-0000","000000-0000000");
        userService.join(user);

        Request request=new Request(user, LocalDateTime.now().plusDays(1l), "서울시 서초구 방배동", "교육봉사", "37.566826", "126.9786567", "1");
        requestService.join(request);

        //when
        assertEquals(requestService.findByUser(user).size(), 1l);
        assertEquals(requestService.findByUser(user).stream().findFirst().get().getId(), request.getId());
    }

    @Test
    void findByStatus() {
        //given
        User user=new User("user1","user@gmail.com","1234","010-0000-0000","000000-0000000");
        userService.join(user);

        Request request=new Request(user, LocalDateTime.now().plusDays(1l), "서울시 서초구 방배동", "교육봉사", "37.566826", "126.9786567", "1");
        requestService.join(request);

        //when
        assertEquals(requestService.findByStatus(RequestStatus.REGISTER).stream().findFirst().get().getId(), request.getId());
        assertEquals(requestService.findByStatus(RequestStatus.CANCEL).size(), 0l);
    }

    @Test
    void findKakaomapList() {
        //given
        User user=new User("user1","user@gmail.com","1234","010-0000-0000","000000-0000000");
        userService.join(user);

        Request request=new Request(user, LocalDateTime.now().plusDays(1l), "서울시 서초구 방배동", "교육봉사", "37.566826", "126.9786567", "1");
        requestService.join(request);

        //when
        assertEquals(requestService.findKakaomapList().size(),1l);
        assertEquals(requestService.findKakaomapList().get(request.getId()), request.getAddress());
    }

    @Test
    void findAll() {
        //given
        User user=new User("user1","user@gmail.com","1234","010-0000-0000","000000-0000000");
        userService.join(user);

        Request request=new Request(user, LocalDateTime.now().plusDays(1l), "서울시 서초구 방배동", "교육봉사", "37.566826", "126.9786567", "1");
        requestService.join(request);

        //when
        assertEquals(requestService.findAll().size(), 1l);
        assertEquals(requestService.findAll().stream().findFirst().get().getId(), request.getId());
    }

    @Test
    void SrvCreateRequest(){
        //given
        User user=new User("user1","user@gmail.com","1234","010-0000-0000","000000-0000000");
        userService.join(user);

        Cookie cookie=new Cookie("userId", String.valueOf(user.getId()));
        RequestDTO requestDTO=new RequestDTO("2025.01.01.00.00", "서울시 서초구 방배동", "노인봉사", "37.566826", "126.9786567", "1");

        //when
        requestService.SrvCreateRequest(cookie, requestDTO);

        //then
        assertEquals(requestService.findAll().size(),1);
        assertEquals(requestService.findAll().stream().findFirst().get().getUserR().getId(), user.getId());
    }

    @Test
    void getDistance(){
        //given
        User user=new User("user1","user@gmail.com","1234","010-0000-0000","000000-0000000");
        userService.join(user);

        Request request=new Request(user, LocalDateTime.now().plusDays(1l), "서울시 서초구 방배동", "교육봉사", "37.566826", "126.9786567", "1");
        requestService.join(request);

        LocationDTO currentLocation=new LocationDTO("126.929244", "37.529231");

        //when
        ArrayList<Double> results=requestService.getDistance(requestService.findAll(), currentLocation);
        System.out.println("[DEBUG] result: "+results.get(0));
        //then
        assertEquals(results.size(), 1);
        assertTrue(results.get(0)<8.5 && results.get(0)>7.5);// 대충 8키로가 나옴. +-0.5로 오차범위
    }

    @Test
    void nearVolun(){//LocationDTO locationDTO, Integer pageSize){
        //given
        User user1=new User("user1","user@gmail.com","1234","010-0000-0000","000000-0000000");
        userService.join(user1);
        Request request1=new Request(user1, LocalDateTime.now().plusDays(1l), "서울시 서초구 방배동", "교육봉사", "37.566826", "126.9786557", "1");
        requestService.join(request1);

        User user2=new User("user12","user2@gmail.com","1234","010-0020-0000","000200-0000000");
        userService.join(user2);
        Request request2=new Request(user2, LocalDateTime.now().plusDays(1l), "서울시 서초구 방배동", "교육봉사", "37.564826", "126.9756567", "1");
        requestService.join(request2);

        User user3=new User("user3","user3@gmail.com","1234","010-0300-0000","300000-0000000");
        userService.join(user3);
        Request request3=new Request(user3, LocalDateTime.now().plusDays(1l), "서울시 서초구 방배동", "교육봉사", "4.566426", "200.9786557", "1");
        requestService.join(request3);

        //when
        List<NearRequestListElementDTO> results=requestService.nearVolun(new LocationDTO("126.9786557", "37.566826"), 2);
        List<NearRequestListElementDTO> results2=requestService.nearVolun(new LocationDTO("126.9786557", "37.566826"), 10);
        //then
        assertEquals(results.size(), 2);
        assertEquals(results.get(0).getId(), request1.getId());
        assertEquals(results.get(1).getId(), request2.getId());
        assertEquals(results2.size(), 3);

    }

    @Test
    void checkIsLocation(){
        //given
        LocationDTO correctDTO=new LocationDTO("127.13213", "37.55");

        LocationDTO wrongDTO1=new LocationDTO("127.13213", "safsag");
        LocationDTO wrongDTO2=new LocationDTO("asfsaf", "37.55");
        LocationDTO wrongDTO3=new LocationDTO("fagaf", "afgdfg");

        LocationDTO wrongDTO4=new LocationDTO("", "37.55");
        LocationDTO wrongDTO5=new LocationDTO("127.165", "");
        LocationDTO wrongDTO6=new LocationDTO("", "");

        //when-then
        assertTrue(requestService.checkIsLocation(correctDTO));

        assertFalse(requestService.checkIsLocation(wrongDTO1));
        assertFalse(requestService.checkIsLocation(wrongDTO2));
        assertFalse(requestService.checkIsLocation(wrongDTO3));

        assertFalse(requestService.checkIsLocation(wrongDTO4));
        assertFalse(requestService.checkIsLocation(wrongDTO5));
        assertFalse(requestService.checkIsLocation(wrongDTO6));
    }
}