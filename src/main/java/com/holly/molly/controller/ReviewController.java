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

@Controller
@RequiredArgsConstructor
public class ReviewController {
    private final RequestService requestService;
    private final AcceptService acceptService;

    private final ReviewService reviewService;

    @Transactional
    @GetMapping("review/r/{id}")
    public String createRequestReview(@PathVariable("id") Long id, Model model){
        model.addAttribute("isRequest", true);
        requestService.findOne(id).get().changeStatus(RequestStatus.REVIEWD);//post이후에 세팅해야하지만 임시로 미리 설정
        return "volun/createReview";
    }

    @Transactional
    @GetMapping("review/a/{id}")
    public String createAcceptReview(@PathVariable("id") Long id, Model model){
        model.addAttribute("isRequest", false);
        acceptService.findOne(id).get().changeStatus(AcceptStatus.REVIEWD);
        return "volun/createReview";
    }

    @Transactional
    @PostMapping("volun/createReview")
    public String uploadReview(ReviewDTO reviewDTO){
        Review review=new Review(reviewDTO.getTitle(), reviewDTO.getContent());
        reviewService.join(review);
        return "redirect:/";
    }

    @GetMapping("review/detail/{id}")
    public String detailReview(@PathVariable("id") Long id, Model model){
        Review review=reviewService.findOne(id).get();
        model.addAttribute("review", review);
        return "volun/detailReview";
    }
}
