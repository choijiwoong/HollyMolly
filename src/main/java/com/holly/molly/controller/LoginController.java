package com.holly.molly.controller;

import com.holly.molly.domain.User;
import com.holly.molly.service.UserService;
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
    public String login(LoginDTO loginDTO, Model model){
        Optional<User> user = userService.signUp(loginDTO);

        if (user.isEmpty()) {
            System.out.println("login fail!");
            return "redirect:/";
        } else if(user.isPresent()) {
            System.out.println("login success!");
            model.addAttribute("user", user);
        }

        return "redirect:/";//문법
    }

}
