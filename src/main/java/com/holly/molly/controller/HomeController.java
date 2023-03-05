package com.holly.molly.controller;

import com.holly.molly.DTO.LocationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequiredArgsConstructor
public class HomeController {
    @GetMapping("/intro")
    public String home(HttpServletResponse response){
        return "homeTemplate/Nhome";
    }
}
