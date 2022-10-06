package com.holly.molly.controller;

import com.holly.molly.domain.Member;
import com.holly.molly.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller//컨트롤러 에너테이션이 있다면 스프링 구동 시 스프링 컨테이너에 인스턴스화한 이후 가지고 있는데, 이들(컨테이너 요소) 자바 빈이라고 한다
public class MemberController {
    private final MemberService memberService;

    @Autowired//스프링 구동 시 Controller와 Service Repository를 자동으로 연결해주며 이를 디펜던시 인젝선이라고 한다.
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members/register")
    public String createForm(){
        return "members/createMemberForm";//템플릿으로 매핑
    }

    @PostMapping("/members/register")//post method
    public String create(MemberForm form){
        Member member =new Member();
        member.setName(form.getName());
        member.setEmailAddress(form.getEmailAddress());
        member.setPassword(form.getPassword());

        memberService.register(member);

        return "redirect:/";
    }
}