package com.holly.molly.controller;

import com.holly.molly.domain.User1;
import com.holly.molly.domain.User2;
import com.holly.molly.service.User1Service;
import com.holly.molly.service.User2Service;
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
    private final User2Service user2Service;
    @GetMapping("/members/login")
    public String loginForm(){ return "members/login"; }

    @GetMapping("/members/register")
    public String registerForm(){ return "members/createUser1Form"; }

    @GetMapping("/members/list")
    public String list(Model model){
        List<User1> user1s=user1Service.findMembers();
        List<User2> user2s=user2Service.findMembers();
        model.addAttribute("user1s", user1s);
        model.addAttribute("user2s", user2s);
        return "members/memberList";
    }

    @PostMapping("/members/register")
    public String register(RegisterDTO registerDTO){
        if(!registerDTO.getIsUser1().isEmpty()) {
            User1 user = new User1();
            user.setName(registerDTO.getName());
            user.setEmail(registerDTO.getEmail());
            user.setBirth(registerDTO.getBirth());
            user.setPassword(registerDTO.getPassword());
            user.setPid(registerDTO.getPid());

            user1Service.join(user);
        } else if(registerDTO.getIsUser1().isEmpty()){
            User2 user=new User2();
            user.setName(registerDTO.getName());
            user.setEmail(registerDTO.getEmail());
            user.setBirth(registerDTO.getBirth());
            user.setPassword(registerDTO.getPassword());
            user.setPid(registerDTO.getPid());

            user2Service.join(user);
        }

        return "redirect:/";
    }

    @PostMapping("/members/login")
    public String login(LoginDTO loginDTO, Model model){
        if(loginDTO.getIsUser1().isPresent()) {
            Optional<User1> user = user1Service.signUp(loginDTO);

            if (user.isEmpty()) {
                System.out.println("login fail!");
                return "redirect:/";
            } else if(user.isPresent()) {
                System.out.println("login success!");
                model.addAttribute("user", user);
            }
        } else if(loginDTO.getIsUser1().isEmpty()){
            Optional<User2> user = user2Service.signUp(loginDTO);

            if (user.isEmpty()) {
                System.out.println("login fail!");
                return "redirect:/";
            } else if(user.isPresent()) {
                System.out.println("login success!");
                model.addAttribute("user", user);
            }
        }
        return "redirect:/";//문법
    }

}
