package com.holly.molly.repository;

import com.holly.molly.domain.QReview;
import com.holly.molly.domain.Review;
import static com.holly.molly.domain.QReview.review;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class ReviewRepository {
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public ReviewRepository(EntityManager em){
        this.em=em;
        this.queryFactory=new JPAQueryFactory(em);
    }

    public void save(Review review){
        em.persist(review);
    }
    public void delete(Review review){ em.remove(review); }

    public Review findOne(Long id){
        return em.find(Review.class, id);
    }

    public List<Review> findAll(){
        return queryFactory.selectFrom(review).fetch();
    }

    public List<Review> findUserRReview(){ return queryFactory.selectFrom(review).where(review.isRequest.eq(true)).fetch(); }

    public List<Review> findUserAReview(){ return queryFactory.selectFrom(review).where(review.isRequest.eq(false)).fetch(); }
}