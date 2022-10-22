package com.holly.molly.repository;

import com.holly.molly.domain.Request;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class RequestRepository {
    private final EntityManager em;

    public void save(Request request){
        em.persist(request);
    }

    public Request findOne(Long id){
        return em.find(Request.class, id);
    }
}
