package com.holly.molly.service;

import com.holly.molly.domain.Review;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ReviewServiceTest {
    @Autowired ReviewService reviewService;
    @Autowired
    EntityManager em;

    @AfterEach
    public void afterEach(){
        em.clear();
    }

    @Test
    void findAll() {
        //given
        Review review=new Review("test", "askla");
        reviewService.join(review);

        //when
        assertEquals(reviewService.findAll().size(), 1l);
        assertEquals(reviewService.findAll().stream().findFirst().get().getId(), review.getId());
        assertEquals(reviewService.findAll().stream().findFirst().get().getTitle(), review.getTitle());
    }
}