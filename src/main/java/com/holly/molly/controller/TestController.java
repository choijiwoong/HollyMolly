package com.holly.molly.controller;

import com.holly.molly.domain.*;
import com.holly.molly.service.AcceptService;
import com.holly.molly.service.RequestService;
import com.holly.molly.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
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
        User admin=new User("admin", "gogogi313@gmail.com", "a", "a", "a");
        userService.join(admin);

        //어드민2 정보 생성
        User admin2=new User("admin2", "zonpark75@gmail.com", "a2", "a2", "a2");
        userService.join(admin2);

        //어드민 쿠키 등록
        Cookie idCookie=new Cookie("userId", String.valueOf(admin.getId()));
        idCookie.setPath("/");
        response.addCookie(idCookie);

        Request request=new Request(admin, LocalDateTime.now().plusMinutes(1l), "서울시 서초구 방배동", "test", "37.566826", "126.9786567");
        requestService.join(request);

        Request request2=new Request(admin, LocalDateTime.now().plusDays(3l), "서울시 강남구", "test", "37.566826", "126.9786567");
        requestService.join(request2);

        Request request3=new Request(admin, LocalDateTime.now().plusDays(5l), "서울시 용산구", "test", "37.566826", "126.9786567");
        requestService.join(request3);

        Accept accept=new Accept(admin2, request);
        acceptService.join(accept);

        return "redirect:/kakaomap";
    }
}
