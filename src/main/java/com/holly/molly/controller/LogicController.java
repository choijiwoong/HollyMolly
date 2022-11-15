package com.holly.molly.controller;

import com.holly.molly.service.AcceptService;
import com.holly.molly.service.RequestService;
import com.holly.molly.service.UserService;
import com.holly.molly.service.VolunService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class LogicController {
    private final UserService userService;
    private final RequestService requestService;
    private final AcceptService acceptService;
    private final VolunService volunService;

    @GetMapping("/volun/createRequest")
    public String createRequest(){
        return "volun/createRequest";
    }
}
