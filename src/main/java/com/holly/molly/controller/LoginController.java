package com.holly.molly.controller;

import com.holly.molly.DTO.LoginDTO;
import com.holly.molly.DTO.UserDTO;
import com.holly.molly.domain.User;
import com.holly.molly.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
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
        model.addAttribute("users", userService.findAll());
        return "members/memberList";
    }

    @PostMapping("/members/register")
    public String register(UserDTO userDTO){
        if(!checkRegisterDTO(userDTO))
            throw new RuntimeException("register format is incorrect!");

        userService.join(new User(userDTO));
        return "redirect:/";
    }

    @PostMapping("/members/login")
    public String login(LoginDTO loginDTO, HttpServletResponse response){
        Optional<User> user;
        if ((user=userService.signUp(loginDTO)).isEmpty()) {//로그인 실패시
            return "members/login";
        }
        //로그인 성공시
        response.addCookie(registerCookie(user.get().getId()));
        return "redirect:/";//웹브라우저는 종료전까지 회원의 id를 서버에 계속 보내준다.
    }

    @GetMapping("/members/logout")
    public String logout(HttpServletResponse response){
        response.addCookie(registerCookie(null));
        return "redirect:/";
    }

    @GetMapping("/admin")
    public String test(HttpServletResponse response){
        //기존 쿠키 제거
        response.addCookie(registerCookie(null));
        //어드민 정보 생성
        User admin=new User("admin", "a", "a", "a", "a");
        userService.join(admin);
        //어드민 쿠키 등록
        response.addCookie(registerCookie(admin.getId()));

        return "redirect:/";
    }

    //<---------내부 함수---------->
    private boolean checkRegisterDTO(UserDTO userDTO) {
        /*
        String nameRegex="^[0-9가-힣a-zA-Z]+$";//영문자, 한글 허용(1개이상)
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
        */
        return true;
    }

    private boolean checkLoginDTO(LoginDTO loginDTO){
        /*
        String emailRegex="\\w+@\\w+\\.\\w+(\\.\\w+)?";
        if(!Pattern.matches(emailRegex, loginDTO.getEmail())) {
            System.out.println("[DEBUG] incorrect email format on login form");
            return false;
        }
        */
        return true;
    }

    private Cookie registerCookie(Long id){
        Cookie idCookie=new Cookie("userId", id==null?null:String.valueOf(id));
        idCookie.setPath("/");
        return idCookie;
    }
}