package com.holly.molly.controller;

import com.holly.molly.domain.*;
import com.holly.molly.service.AcceptService;
import com.holly.molly.service.RequestService;
import com.holly.molly.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class TestController {
    private final UserService userService;
    private final RequestService requestService;
    private final AcceptService acceptService;

    @GetMapping("/test/requestdetail")
    public String createRequest(HttpServletResponse response){
        //기존 쿠키 제거
        Cookie prevCookie=new Cookie("userId", null);
        prevCookie.setPath("/");
        response.addCookie(prevCookie);

        //어드민 정보 생성
        User admin=new User();
        admin.setName("admin");
        admin.setBirth("a");
        admin.setEmail("gogogi313@gmail.com");
        admin.setPassword("a");
        admin.setPid("a");
        admin.setPhone("a");

        userService.join(admin);

        //어드민2 정보 생성
        User admin2=new User();
        admin2.setName("admin2");
        admin2.setBirth("a2");
        admin2.setEmail("zonpark75@gmail.com");
        admin2.setPassword("a2");
        admin2.setPid("a2");
        admin2.setPhone("a2");

        userService.join(admin2);

        //어드민 쿠키 등록
        Cookie idCookie=new Cookie("userId", String.valueOf(admin.getId()));
        idCookie.setPath("/");
        response.addCookie(idCookie);

        Request request=new Request();
        request.setAddress("서울시 서초구 방배동");
        request.setStatus(RequestStatus.REGISTER);
        request.setReqtime(LocalDateTime.now().minusMinutes(1));
        request.setContent("test");
        request.setUserR(admin);
        request.setExectime(LocalDateTime.now().plusMinutes(1l));
        requestService.join(request);


        Accept accept=new Accept();
        accept.setStatus(AcceptStatus.REGISTER);
        accept.setAcctime(LocalDateTime.now());
        accept.setUserA(admin2);
        accept.setRequest(request);

        acceptService.join(accept);

        return "redirect:/kakaomap";
    }
}
