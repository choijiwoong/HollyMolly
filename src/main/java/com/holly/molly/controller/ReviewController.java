package com.holly.molly.controller;

import com.holly.molly.DTO.ReviewDTO;
import com.holly.molly.domain.AcceptStatus;
import com.holly.molly.domain.RequestStatus;
import com.holly.molly.domain.Review;
import com.holly.molly.service.AcceptService;
import com.holly.molly.service.RequestService;
import com.holly.molly.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ReviewController {
    private final RequestService requestService;
    private final AcceptService acceptService;
    private final ReviewService reviewService;

    @Transactional
    @GetMapping("review/r/{id}")//봉사요청자 리뷰생성 페이지 이동. 이미 리뷰 완료한것으로 임시처리
    public String createRequestReview(@PathVariable("id") Long id, Model model){
        model.addAttribute("isRequest", true);
        requestService.findOne(id).get().changeStatus(RequestStatus.REVIEWD);//post이후에 세팅해야하지만 임시로 미리 설정
        return "volun/createReview";
    }

    @Transactional
    @GetMapping("review/a/{id}")//봉사수락자 리뷰생성 페이지 이동. 이미 리뷰 완료한것으로 임시처리
    public String createAcceptReview(@PathVariable("id") Long id, Model model){
        model.addAttribute("isRequest", false);
        acceptService.findOne(id).get().changeStatus(AcceptStatus.REVIEWD);
        return "volun/createReview";
    }

    @Transactional
    @PostMapping("volun/createReview")//리뷰 등록(봉사요청자와 봉사수락자 구분X)
    public String uploadReview(ReviewDTO reviewDTO){
        reviewService.SrvRegisterReview(reviewDTO);
        return "redirect:/";
    }

    @GetMapping("review/detail/{id}")//봉사리뷰의 상세페이지
    public String detailReview(@PathVariable("id") Long id, Model model){
        Optional<Review> review;
        if((review=reviewService.findOne(id)).isEmpty())
            throw new RuntimeException("리뷰 상세정보를 찾는 중 리뷰에 대한 정보를 찾을 수 없습니다.");

        model.addAttribute("review", review.get());
        return "volun/detailReview";
    }
}
