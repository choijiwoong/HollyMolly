package com.holly.molly.controller;

import com.holly.molly.service.AsyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final AsyncService asyncService;
    @GetMapping("/intro")
    public String home(){
        asyncService.join();
        return "homeTemplate/Nhome";
    }
}
