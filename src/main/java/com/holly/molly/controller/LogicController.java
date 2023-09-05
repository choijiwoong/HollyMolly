package com.holly.molly.controller;

import com.holly.molly.DTO.CommentDTO;
import com.holly.molly.DTO.LocationDTO;
import com.holly.molly.DTO.NearRequestListElementDTO;
import com.holly.molly.DTO.RequestDTO;
import com.holly.molly.domain.*;
import com.holly.molly.service.AcceptService;
import com.holly.molly.service.AsyncService;
import com.holly.molly.service.RequestCommentService;
import com.holly.molly.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class LogicController {//기술로직 컨트롤러
    private final RequestService requestService;
    private final AcceptService acceptService;
    private final RequestCommentService requestCommentService;
    private final AsyncService asyncService;

    @GetMapping("/volun/createRequest")//봉사요청 시 요청페이지로 전환
    public String createRequest() {
        return "volun/createRequest";
    }

    @PostMapping("/volun/createRequest")//DTO, 유저정보 이용 request생성 및 등록
    public String saveRequest(RequestDTO requestDTO, @CookieValue(value = "userId", required = false) Cookie cookie) {
        requestService.SrvCreateRequest(cookie, requestDTO);
        return "redirect:/";
    }

    @GetMapping("/volun/requestList")//봉사요청 리스트업 페이지
    public String requestList(Model model) {
        model.addAttribute("requests", requestService.findByStatus(RequestStatus.REGISTER));
        return "volun/requestList";
    }

    @GetMapping("/volun/accept/{requestid}")//requestId, 유저정보->봉사요청을 수락
    public String accept(@PathVariable("requestid") Long requestId, @CookieValue(value = "userId", required = false) Cookie cookie) {
        acceptService.SrvCreateAccept(requestId, cookie);
        return "redirect:/";
    }

    @GetMapping("/volun/acceptList")
    public String acceptList(Model model) {//수락된 요청들 리스트업 페이지
        model.addAttribute("accepts", acceptService.findByStatus(AcceptStatus.REGISTER));
        return "volun/acceptList";
    }

    @GetMapping("/volun")//지도api이용, 신청가능 모든 요청 표시
    public String showMap(Model model, @CookieValue(value = "userId", required = false) Cookie cookie, @CookieValue(value = "latitude", required = false) Cookie cookie2, @CookieValue(value = "longitude", required = false) Cookie cookie3) {
        HashMap<Long, String> kakaomapList = requestService.findKakaomapList();//requestId, address
        ArrayList<Long> ids=new ArrayList<>();
        ArrayList<String> addresses=new ArrayList<>();
        for(Map.Entry<Long, String> mapElement: kakaomapList.entrySet()){
            ids.add(mapElement.getKey());
            addresses.add(mapElement.getValue());
        }

        List<NearRequestListElementDTO> nearVoluns=requestService.nearVolun(new LocationDTO(cookie2.getValue(), cookie3.getValue()), 10);
        model.addAttribute("nearVoluns", nearVoluns);
        model.addAttribute("addresses", addresses);//kakaomap에 마크표시위함
        model.addAttribute("ids", ids);//kakaomap에 마크표시위함

        return "apis/kakaoMap";
    }

    @GetMapping("/volun/detailRequest/{requestid}")//request내용 자세히보기
    public String detailRequest(@PathVariable("requestid") Long requestId, Model model) {
        model.addAttribute("request", requestService.findOne(requestId).get());//comment 추후 사용. 리팩토링 필요
        return "volun/detailRequest";
    }

    @PostMapping("/comment/request")//request에 댓글 등록
    public String makeCommentRequest(@CookieValue(value = "userId", required = false) Cookie cookie, CommentDTO commentDTO, Model model) {
        model.addAttribute("request", requestCommentService.SrvCreateRequestComment(cookie, commentDTO));
        return "volun/detailRequest";
    }

    @GetMapping("/request/emergency/{requestId}")//봉사요청에 긴급상황 발생시 대응 서비스 가동
    public String emergency(@PathVariable("requestId") Long requestId){
        asyncService.emergency(requestId);
        return "redirect:/";
    }
}
