package com.holly.molly.controller;

import com.holly.molly.domain.User;
import com.holly.molly.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class LoginController {
    private final UserService userService;
    @GetMapping("/members/login")
    public String loginForm(){ return "members/login"; }

    @GetMapping("/members/register")
    public String registerForm(){ return "members/createUser1Form"; }

    @GetMapping("/members/list")
    public String list(Model model){
        List<User> users = userService.findMembers();
        model.addAttribute("users", users);
        return "members/memberList";
    }

    @PostMapping("/members/register")
    public String register(RegisterDTO registerDTO){
        User user = new User();
        user.setName(registerDTO.getName());
        user.setEmail(registerDTO.getEmail());
        user.setBirth(registerDTO.getBirth());
        user.setPassword(registerDTO.getPassword());
        user.setPid(registerDTO.getPid());

        userService.join(user);
        return "redirect:/";
    }

    @PostMapping("/members/login")
    public String login(LoginDTO loginDTO, Model model, HttpServletResponse response){
        Optional<User> user = userService.signUp(loginDTO);
        if (user.isEmpty()) {//로그인 실패시
            System.out.println("login fail!");
            return "members/login";
        }

        //로그인 성공시
        System.out.println("Login Success");
        Cookie idCookie=new Cookie("userId", String.valueOf(user.get().getId()));
        idCookie.setPath("/");
        response.addCookie(idCookie);
        return "redirect:/";//웹브라우저는 종료전까지 회원의 id를 서버에 계속 보내준다.
    }

    @GetMapping("/members/logout")
    public String logout(HttpServletResponse response){
        Cookie idCookie=new Cookie("userId", null);
        idCookie.setPath("/");
        response.addCookie(idCookie);

        return "redirect:/";
    }
}