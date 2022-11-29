package com.holly.molly.repository;

import com.holly.molly.domain.Review;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ReviewRepositoryTest {
    @Autowired ReviewRepository reviewRepository;

    @Test
    void findOne() {
        //given
        Review review=new Review();
        review.setTitle("노인복지관 봉사리뷰");
        review.setContent("재밌었다");

        reviewRepository.save(review);

        //when
        Review review2=reviewRepository.findOne(review.getId());

        //then
        assertEquals(review2.getTitle(), review.getTitle());
    }

    @Test
    void findAll() {
        //given
        Review review=new Review();
        review.setTitle("노인복지관 봉사리뷰");
        review.setContent("재밌었다");

        reviewRepository.save(review);

        //when
        List<Review> reviews=reviewRepository.findAll();

        //then
        assertEquals(reviews.stream().findFirst().get().getId(), review.getId());
    }
}