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
import java.util.regex.Pattern;

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
        if(!checkRegisterDTO(registerDTO)){
            throw new RuntimeException("register format is incorrect!");
        }

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

    //<---------내부 함수---------->
    private boolean checkRegisterDTO(RegisterDTO registerDTO) {
        String nameRegex="^[가-힣a-zA-Z]+$";//영문자, 한글 허용(1개이상)
        if(!Pattern.matches(nameRegex, registerDTO.getName())) {
            System.out.println("[DEBUG] incorrect name format on register form");
            return false;
        }

        String emailRegex="\\w+@\\w+\\.\\w+(\\.\\w+)?";
        if(!Pattern.matches(emailRegex, registerDTO.getEmail())) {
            System.out.println("[DEBUG] incorrect email format on register form");
            return false;
        }

        String phoneRegex="^[0-9]{2,3}-[0-9]{3,4}-[0-9]{4}$";
        if(!Pattern.matches(phoneRegex, registerDTO.getPhone())) {
            System.out.println("[DEBUG] incorrect phone format on register form");
            return false;
        }

        String birthRegex="^[0-9]{4}\\.[0-9]{2}\\.[0-9]{2}$";
        if(!Pattern.matches(birthRegex, registerDTO.getBirth())) {
            System.out.println("[DEBUG] incorrect birth format on register form");
            return false;
        }

        String pidRegex="[0-9]{6}-[1-4][0-9]{6}";
        if(!Pattern.matches(pidRegex, registerDTO.getPid())) {
            System.out.println("[DEBUG] incorrect pid format on register form");
            return false;
        }

        return true;
    }

    private boolean checkLoginDTO(LoginDTO loginDTO){
        String emailRegex="\\w+@\\w+\\.\\w+(\\.\\w+)?";
        if(!Pattern.matches(emailRegex, loginDTO.getEmail())) {
            System.out.println("[DEBUG] incorrect email format on login form");
            return false;
        }

        return true;
    }
}