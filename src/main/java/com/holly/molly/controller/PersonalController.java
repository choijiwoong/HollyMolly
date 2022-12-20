package com.holly.molly.controller;

import com.holly.molly.domain.User;
import com.holly.molly.service.AcceptService;
import com.holly.molly.service.RequestService;
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
public class PersonalController {
    private final UserService userService;
    private final RequestService requestService;
    private final AcceptService acceptService;

    @GetMapping("/mypage/recentHistory")
    public String mypage(Model model, @CookieValue(value="userId", required = false) Cookie cookie){
        User user=parseUserCookie(cookie);
        model.addAttribute("user", user);
        return "myPage/recentHistory";
    }

    //<----내부로직---->
    private User parseUserCookie(Cookie cookie){
        Optional<User> userInfo=userService.findOne(Long.valueOf(cookie.getValue()));
        if(userInfo.isEmpty()){
            throw new RuntimeException("cannot find current user information on cookie");
        }
        return userInfo.get();
    }
}
