package com.holly.molly.controller;

import com.holly.molly.domain.*;
import com.holly.molly.service.AcceptService;
import com.holly.molly.service.RequestService;
import com.holly.molly.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.websocket.server.PathParam;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class LogicController {
    private final UserService userService;
    private final RequestService requestService;
    private final AcceptService acceptService;

    @GetMapping("/volun/createRequest")
    public String createRequest(){
        return "volun/createRequest";
    }

    @PostMapping("/volun/createRequest")
    public String saveRequest(RequestDTO requestDTO, @CookieValue(value="userId", required = false) Cookie cookie){
        Request request=new Request();
        User userInfo=parseUserCookie(cookie);

        request.setUserR(userInfo);
        request.setReqtime(LocalDateTime.now());
        request.setStatus(RequestStatus.REGISTER);
        request.setContent(requestDTO.getContent());

        String exectime= requestDTO.getExectime();//2022.11.16.14.24
        System.out.println(exectime);
        if(exectime.length()!=16){
            System.out.println("check time format");
            return "redirect:/";
        }
        Integer year=Integer.parseInt(exectime.substring(0,4));
        Integer month=Integer.parseInt(exectime.substring(5,7));
        Integer date=Integer.parseInt(exectime.substring(8,10));
        Integer hour=Integer.parseInt(exectime.substring(11,13));
        Integer minute=Integer.parseInt(exectime.substring(14,16));
        System.out.println(year);
        System.out.println(month);
        System.out.println(date);
        System.out.println(hour);
        System.out.println(minute);

        request.setExectime(LocalDateTime.of(year, month, date, hour, minute));
        request.setLocation(requestDTO.getLocation());

        userInfo.getRequests().add(request);

        requestService.join(request);

        return "redirect:/";
    }

    @GetMapping("/volun/requestList")
    public String requestList(Model model){
        List<Request> requests=requestService.findAll();
        System.out.println("request list length: "+requests.size());
        model.addAttribute("requests", requests);
        return "volun/requestList";
    }

    @GetMapping("/volun/accept/{requestid}")
    public String accept(@PathVariable("requestid") Long requestId, @CookieValue(value="userId", required = false) Cookie cookie){
        System.out.println("accept requestId is "+requestId);
        Request request=requestService.findOne(requestId);

        Accept accept=new Accept();
        accept.setUserA(parseUserCookie(cookie));
        accept.setAcctime(LocalDateTime.now());
        accept.setStatus(AcceptStatus.REGISTER);
        request.setStatus(RequestStatus.ACCEPT);

        accept.setRequest(request);
        request.setAccept(accept);

        acceptService.join(accept);

        return "redirect:/";
    }

    @GetMapping("/volun/acceptList")
    public String acceptList(Model model){
        List<Accept> accepts=acceptService.findAll();
        System.out.println("accepts list length: "+accepts.size());
        model.addAttribute("accepts", accepts);
        return "volun/acceptList";
    }

    private User parseUserCookie(Cookie cookie){
        Optional<User> userInfo=Optional.of(userService.findOne(Long.valueOf(cookie.getValue())));
        if(userInfo.isEmpty()){
            throw new RuntimeException("cannot find current user information on cookie");
        }
        return userInfo.get();
    }
}
