package com.holly.molly.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.holly.molly.domain.Member;

@Controller
public class HomeController {
    @GetMapping("/")//지정X 시 index.html로 이동. 우선순위가 자바 컨테이너에서 먼저 찾기에 현재의 컨트롤러 매핑을 인지
    public String home(){ return "home"; }
}
