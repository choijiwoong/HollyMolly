package com.holly.molly.repository;

import com.holly.molly.domain.Accept;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class AcceptRepository {
    private final EntityManager em;

    public void save(Accept accept){ em.persist(accept); }

    public Accept findOne(Long id){ return em.find(Accept.class, id); }
}
