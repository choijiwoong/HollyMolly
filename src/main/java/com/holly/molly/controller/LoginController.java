package com.holly.molly.controller;

import com.holly.molly.domain.User1;
import com.holly.molly.service.User1Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class LoginController {
    private final User1Service user1Service;
    @GetMapping("/members/login")
    public String loginForm(){ return "members/login"; }

    @GetMapping("/members/register")
    public String registerForm(){ return "members/createUser1Form"; }

    @GetMapping("/members/list")
    public String list(Model model){
        List<User1> user1s=user1Service.findMembers();
        model.addAttribute("user1s", user1s);
        return "members/memberList";
    }

    @PostMapping("/members/register")
    public String register(RegisterDTO registerDTO){
        User1 user = new User1();
        user.setName(registerDTO.getName());
        user.setEmail(registerDTO.getEmail());
        user.setBirth(registerDTO.getBirth());
        user.setPassword(registerDTO.getPassword());
        user.setPid(registerDTO.getPid());

        user1Service.join(user);

        return "redirect:/";
    }

    @PostMapping("/members/login")
    public String login(LoginDTO loginDTO, Model model){
        Optional<User1> user=user1Service.signUp(loginDTO);
        if (user.isEmpty()) {
            System.out.println("login fail!");
            return "redirect:/";
        }
        System.out.println("login success!");
        model.addAttribute("user1", user);
        return "/members/memberList";
    }

}
