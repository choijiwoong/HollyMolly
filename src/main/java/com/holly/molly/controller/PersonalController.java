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
public class PersonalController {
    private final UserService userService;

    @GetMapping("/mypage/recentHistory")
    public String mypage(Model model, @CookieValue(value="userId", required = false) Cookie cookie){
        model.addAttribute("user", userService.parseUserCookie(cookie));
        return "myPage/recentHistory";
    }
}
