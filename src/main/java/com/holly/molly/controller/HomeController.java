package com.holly.molly.controller;

import com.holly.molly.service.AsyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class HomeController {//첫 홈페이지 관련 컨트롤러
    private final AsyncService asyncService;
    @GetMapping("/intro")
    public String home(){
        asyncService.join();//[DEBUG]임시로 동기화 서비스를 첫 페이지 링크 시 가동
        return "homeTemplate/Nhome";
    }

    @GetMapping("/rest")
    public String restTest(){
        return "homeTemplate/restTestPage";
    }
}
