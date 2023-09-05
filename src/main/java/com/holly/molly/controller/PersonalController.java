package com.holly.molly.controller;

import com.holly.molly.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;

@Controller
@RequiredArgsConstructor
public class PersonalController {//상호작용없이 개인정보 조작에 사용할 컨트롤러
    private final UserService userService;

    @GetMapping("/mypage/recentHistory")//쿠키 기반, 신청한 기록 확인페이지
    public String mypage(Model model, @CookieValue(value="userId", required = false) Cookie cookie){
        model.addAttribute("user", userService.parseUserCookie(cookie));
        return "myPage/recentHistory";
    }
}
