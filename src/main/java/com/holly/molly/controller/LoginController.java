package com.holly.molly.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/members/login")
    public String login(){ return "members/login"; }

    @GetMapping("/members/register")
    public String register(){ return "members/createMemberForm"; }
}
