package com.holly.molly.controller;

import com.holly.molly.DTO.LocationDTO;
import com.holly.molly.DTO.LoginDTO;
import com.holly.molly.DTO.UserDTO;
import com.holly.molly.domain.User;
import com.holly.molly.service.RequestService;
import com.holly.molly.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class LoginController {
    private final UserService userService;
    private final RequestService requestService;
    @GetMapping("/members/login")//로그인 폼
    public String loginForm(){ return "members/login"; }

    @GetMapping("/members/register")//회원가입 폼
    public String registerForm(){ return "members/createUser1Form"; }

    @GetMapping("/members/list")//멤버정보 리스트
    public String list(Model model){
        model.addAttribute("users", userService.findAll());
        return "members/memberList";
    }

    @PostMapping("/members/register")//회원가입 POST시 DB저장 및 쿠키 초기화(로그아웃 시 초기화로 충분하지만, 회원가입시 정보 초기화 규칙)
    public String register(HttpServletResponse response, UserDTO userDTO){
        response.addCookie(userService.createCookie(null));
        if(!checkRegisterDTO(userDTO))
            throw new RuntimeException("register format is incorrect!");

        userService.join(new User(userDTO));
        return "redirect:/";
    }

    @PostMapping("/members/login")//로그인 POST 시 확인 후 쿠키등록
    public String login(LoginDTO loginDTO, HttpServletResponse response){
        Optional<User> user;
        if ((user=userService.signUp(loginDTO)).isEmpty()) {//로그인 실패시
            return "members/login";
        }
        //로그인 성공시
        response.addCookie(userService.createCookie(user.get().getId()));

        //GPS정보 캐시 등록
        LocationDTO locationDTO=new LocationDTO(loginDTO.getLongitude(), loginDTO.getLatitude());
        if(!requestService.checkIsLocation(locationDTO)){//유효성검사
            throw new RuntimeException("GPS정보를 읽을 수 없습니다.");
        }
        response.addCookie(userService.createCookieLng(locationDTO.getLongitude()));
        response.addCookie(userService.createCookieLat(locationDTO.getLatitude()));

        return "redirect:/";//웹브라우저는 종료전까지 회원의 id를 서버에 계속 보내준다.
    }

    @GetMapping("/members/logout")//로그아웃 쿠키 초기화
    public String logout(HttpServletResponse response){
        response.addCookie(userService.createCookie(null));
        return "redirect:/";
    }

    //<---------내부 함수---------->
    private boolean checkRegisterDTO(UserDTO userDTO) {//회원가입 시 입력조건 확인
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

    private boolean checkLoginDTO(LoginDTO loginDTO){//로그인 시 입력조건 확인
        /*
        String emailRegex="\\w+@\\w+\\.\\w+(\\.\\w+)?";
        if(!Pattern.matches(emailRegex, loginDTO.getEmail())) {
            System.out.println("[DEBUG] incorrect email format on login form");
            return false;
        }
        */
        return true;
    }
}