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

    @GetMapping("/")//지정X 시 index.html로 이동. 우선순위가 자바 컨테이너에서 먼저 찾기에 현재의 컨트롤러 매핑을 인지
    public String home(Model model, @CookieValue(value="userId", required = false) Cookie cookie){
        try {
            //asyncService.checkVolunStatus();//async task
        } catch(RuntimeException e){
            e.printStackTrace();
        }

        if(cookie==null || cookie.getValue().equals("")){
            System.out.println("NotExist Cookie");//ㄴㄴ
            model.addAttribute("user", Optional.empty());
        } else{
            System.out.println("Exist Cookie");
            model.addAttribute("user", Optional.of(userService.findOne(Long.valueOf(cookie.getValue()))));//cookie의 string(id)를 integer로
        }

        List<Review> reviews=reviewService.findAll();
        model.addAttribute("reviews", reviews);
        model.addAttribute("reviewSize", reviews.size());

        return "homeTemplate/home";
    }

    @GetMapping("/introducePage")//지정X 시 index.html로 이동. 우선순위가 자바 컨테이너에서 먼저 찾기에 현재의 컨트롤러 매핑을 인지
    public String intro(){ return "homeTemplate/post/firstStep"; }
}
