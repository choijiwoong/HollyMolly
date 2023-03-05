package com.holly.molly.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class HomeController {
    @GetMapping("/intro")
    public String home(){
        return "homeTemplate/Nhome";
    }
}
