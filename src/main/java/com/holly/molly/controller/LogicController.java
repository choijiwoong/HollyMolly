package com.holly.molly.controller;

import com.holly.molly.DTO.CommentDTO;
import com.holly.molly.DTO.RequestDTO;
import com.holly.molly.domain.*;
import com.holly.molly.service.AcceptService;
import com.holly.molly.service.RequestCommentService;
import com.holly.molly.service.RequestService;
import com.holly.molly.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class LogicController {
    private final RequestService requestService;
    private final AcceptService acceptService;
    private final RequestCommentService requestCommentService;
    private final UserService userService;

    @GetMapping("/volun/createRequest")
    public String createRequest() {
        return "volun/createRequest";
    }

    @PostMapping("/volun/createRequest")
    public String saveRequest(RequestDTO requestDTO, @CookieValue(value = "userId", required = false) Cookie cookie) {
        requestService.SrvCreateRequest(cookie, requestDTO);
        return "redirect:/";
    }

    @GetMapping("/volun/requestList")
    public String requestList(Model model) {
        model.addAttribute("requests", requestService.findByStatus(RequestStatus.REGISTER));
        return "volun/requestList";
    }

    @GetMapping("/volun/accept/{requestid}")
    public String accept(@PathVariable("requestid") Long requestId, @CookieValue(value = "userId", required = false) Cookie cookie) {
        acceptService.SrvCreateAccept(requestId, cookie);
        return "redirect:/";
    }

    @GetMapping("/volun/acceptList")
    public String acceptList(Model model) {
        model.addAttribute("accepts", acceptService.findByStatus(AcceptStatus.REGISTER));
        return "volun/acceptList";
    }

    @GetMapping("/kakaomap")
    public String showMap(Model model, @CookieValue(value = "userId", required = false) Cookie cookie) {
        HashMap<Long, String> kakaomapList = requestService.findKakaomapList();

        ArrayList<Long> ids=new ArrayList<>();
        ArrayList<String> addresses=new ArrayList<>();
        for(Map.Entry<Long, String> mapElement: kakaomapList.entrySet()){
            ids.add(mapElement.getKey());
            addresses.add(mapElement.getValue());
        }

        model.addAttribute("addresses", addresses);
        model.addAttribute("ids", ids);
        model.addAttribute("userName", userService.parseUserCookie(cookie).getName());
        model.addAttribute("requests", requestService.findByStatus(RequestStatus.REGISTER));
        return "apis/kakaoMap";
    }

    @ResponseBody
    @RequestMapping("/getNearVolun")
    public String valueTest(){
        String value = "테스트 String";
        return value;
    }

    @GetMapping("/volun/detailRequest/{requestid}")
    public String detailRequest(@PathVariable("requestid") Long requestId, Model model) {
        model.addAttribute("request", requestService.findOne(requestId).get());//comment 추후 사용. 리팩토링 필요
        return "volun/detailRequest";
    }

    @PostMapping("/comment/request")
    public String makeCommentRequest(@CookieValue(value = "userId", required = false) Cookie cookie, CommentDTO commentDTO, Model model) {
        model.addAttribute("request", requestCommentService.SrvCreateRequestComment(cookie, commentDTO));
        return "volun/detailRequest";
    }
}
