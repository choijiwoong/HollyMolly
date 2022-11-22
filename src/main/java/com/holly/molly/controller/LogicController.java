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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

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
        if(!checkLocaldatetime(exectime)){
            throw new RuntimeException("check time format");
        }

        Integer year=Integer.parseInt(exectime.substring(0,4));
        Integer month=Integer.parseInt(exectime.substring(5,7));
        Integer date=Integer.parseInt(exectime.substring(8,10));
        Integer hour=Integer.parseInt(exectime.substring(11,13));
        Integer minute=Integer.parseInt(exectime.substring(14,16));

        request.setExectime(LocalDateTime.of(year, month, date, hour, minute));
        request.setAddress(requestDTO.getAddress());

        userInfo.getRequests().add(request);

        requestService.join(request);

        return "redirect:/";
    }

    @GetMapping("/volun/requestList")
    public String requestList(Model model){
        List<Request> requests=requestService.findByStatus(RequestStatus.REGISTER);
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

        if(checkDuplicatedUserRequestAccept(request, accept)){
            throw new RuntimeException("Request user & Accept user is same");
        }

        accept.setRequest(request);
        request.setAccept(accept);

        acceptService.join(accept);

        return "redirect:/";
    }

    @GetMapping("/volun/acceptList")
    public String acceptList(Model model){
        List<Accept> accepts=acceptService.findByStatus(AcceptStatus.REGISTER);
        System.out.println("accepts list length: "+accepts.size());
        model.addAttribute("accepts", accepts);
        return "volun/acceptList";
    }

    @GetMapping("/kakaomap")
    public String showMap(Model model){//보완필요
        List<Request> requests=requestService.findAll().stream().filter(
                r->r.getStatus().equals(RequestStatus.ACCEPT)).toList();
        model.addAttribute("addresses", requests);

        return "apis/kakaoMap";
    }

    //<-----내부 로직------>

    private User parseUserCookie(Cookie cookie){
        Optional<User> userInfo=Optional.of(userService.findOne(Long.valueOf(cookie.getValue())));
        if(userInfo.isEmpty()){
            throw new RuntimeException("cannot find current user information on cookie");
        }
        return userInfo.get();
    }

    //<-------내부 함수-------->
    private boolean checkLocaldatetime(String time){
        /*
        String pattern="^[0-9]{4}-[0-9]{2}-[0-9]{2}-[0-9]{2}-[0-9]{2}$";
        if(!Pattern.matches(pattern, time))
            return false;
         */
        return true;
    }

    private boolean checkDuplicatedUserRequestAccept(Request request, Accept accept){
        if(request.getUserR().getId()==accept.getUserA().getId())
            return true;
        return false;
    }
}
