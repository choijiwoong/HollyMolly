package com.holly.molly.service;

import com.holly.molly.DTO.ReviewDTO;
import com.holly.molly.domain.Review;
import com.holly.molly.domain.User;
import com.holly.molly.repository.ReviewRepository;
import com.holly.molly.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    //**********DB**********
    @Transactional
    public Long join(Review review){
        reviewRepository.save(review);
        return review.getId();
    }

    @Transactional
    public Long delete(Review review){
        if(Optional.ofNullable(reviewRepository.findOne(review.getId())).isEmpty()){
            throw new RuntimeException("삭제하려는 Accept가 존재하지 않습니다.");
        }
        reviewRepository.delete(review);
        return review.getId();
    }

    public Optional<Review> findOne(Long id){ return Optional.ofNullable(reviewRepository.findOne(id)); }

    public List<Review> findAll(){
        return reviewRepository.findAll();
    }

    //***********서비스로직************
    @Transactional
    public void SrvRegisterReview(ReviewDTO reviewDTO){//DTO이용 DB에 리뷰 저장
        this.join(new Review(reviewDTO.getTitle(), reviewDTO.getContent(), reviewDTO.getIsRequest()));
    }
}
