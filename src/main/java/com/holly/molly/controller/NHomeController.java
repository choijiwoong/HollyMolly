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

    @GetMapping("/")//지정X 시 index.html로 이동. 우선순위가 자바 컨테이너에서 먼저 찾기에 현재의 컨트롤러 매핑을 인지
    public String home(Model model, @CookieValue(value="userId", required = false) Cookie cookie){
        model.addAttribute("user", cookie==null||cookie.getValue().equals("")
                ? Optional.empty()
                : userService.findOne(Long.valueOf(cookie.getValue()))
        );
        return "homeTemplate/home";
    }

    @GetMapping("/introducePage")//지정X 시 index.html로 이동. 우선순위가 자바 컨테이너에서 먼저 찾기에 현재의 컨트롤러 매핑을 인지
    public String intro(){ return "homeTemplate/post/firstStep"; }
}
