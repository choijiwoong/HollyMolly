package com.holly.molly.controller;

import com.holly.molly.domain.*;
import com.holly.molly.service.AcceptService;
import com.holly.molly.service.RequestService;
import com.holly.molly.service.UserService;
import com.holly.molly.service.VolunService;
import com.sun.istack.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class LogicController {
    private final UserService userService;
    private final RequestService requestService;
    private final AcceptService acceptService;
    private final VolunService volunService;

    @GetMapping("/volun/createRequest")
    public String createRequest(){
        return "volun/createRequest";
    }

    @PostMapping("/volun/createRequest")
    public String saveRequest(VolunDTO volunDTO, @CookieValue(value="userId", required = false) Cookie cookie){
        Request request=new Request();
        Optional<User> userInfo=Optional.of(userService.findOne(Long.valueOf(cookie.getValue())));
        if(userInfo.isEmpty()){
            System.out.println("cannot find current user information on cookie");
            return "redirect:/";
        }

        request.setUserR(userInfo.get());
        request.setReqtime(LocalDateTime.now());
        request.setStatus(RequestStatus.REGISTER);

        Volun volun=new Volun();//완성되지 않은 Volun객체 생성 및 저장, Accept시 Volun완성 예정 고로 status체크가 중요. 실제 Volun의 저장은 Accept시 같이 수행 예정
        volun.setStatus(VolunStatus.REQUEST);
        String exectime= volunDTO.getExectime();
        System.out.println(exectime);
        String[] timeInfo=exectime.split(".", 5);
        System.out.println(timeInfo[0]);
        volun.setExectime(LocalDateTime.of(Integer.parseInt(timeInfo[0]), Integer.parseInt(timeInfo[1]), Integer.parseInt(timeInfo[2]), Integer.parseInt(timeInfo[3]), Integer.parseInt(timeInfo[4])));
        volun.setAddress(volunDTO.getAddress());

        //연관관계매핑
        request.setVolunR(volun);
        userInfo.get().getRequests().add(request);

        requestService.join(request);

        return "redirect:/";
    }

    @GetMapping("/volun/requestList")
    public String requestList(Model model){
        List<Request> requests=requestService.findAll();
        model.addAttribute("requests", requests);
        return "volun/requestList";
    }
}
