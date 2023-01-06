package com.holly.molly.controller;

import com.holly.molly.domain.Review;
import com.holly.molly.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final UserService userService;
    private final AsyncService asyncService;
    private final ReviewService reviewService;

    @GetMapping("/intro")
    public String home(){
        return "homeTemplate/Nhome";
    }
}
