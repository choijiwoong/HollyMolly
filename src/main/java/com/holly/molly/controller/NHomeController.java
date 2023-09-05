package com.holly.molly.controller;

import com.holly.molly.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class NHomeController {
    private final UserService userService;

    @GetMapping("/")//로그인 여부 정보전달, 기본 매핑(기본 매핑을 /index에서 homeTemplate/home으로 변경)
    public String home(Model model, @CookieValue(value="userId", required = false) Cookie cookie){
        model.addAttribute("user", cookie==null||cookie.getValue().equals("")
                ? Optional.empty()
                : userService.findOne(Long.valueOf(cookie.getValue()))
        );
        return "homeTemplate/home";
    }

    @GetMapping("/introducePage")//설명 페이지
    public String intro(){ return "homeTemplate/post/firstStep"; }
}
