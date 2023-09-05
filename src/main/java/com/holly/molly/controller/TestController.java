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
public class TestController {//[DEBUG]테스트를 위한 별도 디버깅 컨트롤러
    private final UserService userService;
    private final RequestService requestService;
    private final AcceptService acceptService;

    @GetMapping("/test/requestdetail")
    public String createRequest(HttpServletResponse response){
        //기존 쿠키 제거
        Cookie prevCookie=new Cookie("userId", null);
        prevCookie.setPath("/");
        response.addCookie(prevCookie);

        return "redirect:/";
    }
}
