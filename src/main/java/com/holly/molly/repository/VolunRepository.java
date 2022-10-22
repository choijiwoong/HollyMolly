package com.holly.molly.repository;

import com.holly.molly.domain.Volun;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class VolunRepository {
    private static EntityManager em;

    public void save(Volun volun){ em.persist(volun); }

    public Volun findOne(Long id){ return em.find(Volun.class, id); }
}
