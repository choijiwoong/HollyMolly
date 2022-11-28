package com.holly.molly.repository;

import com.holly.molly.domain.Review;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class ReviewRepository {
    @PersistenceContext
    private EntityManager em;

    public void save(Review review){
        em.persist(review);
    }
    public void delete(Review review){ em.remove(review); }

    public Review findOne(Long id){
        return em.find(Review.class, id);
    }

    public List<Review> findAll(){
        return em.createQuery("select r from Review r", Review.class).getResultList();
    }

    public void clear(){
        em.clear();
    }
}
