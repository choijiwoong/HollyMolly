package com.holly.molly.repository;

import com.holly.molly.domain.Accept;
import com.holly.molly.domain.AcceptStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AcceptRepository {
    private final EntityManager em;

    public void save(Accept accept){ em.persist(accept); }

    public Accept findOne(Long id){ return em.find(Accept.class, id); }

    public List<Accept> findByAcctime(LocalDateTime acctime){
        return em.createQuery("select a from Accept a where a.acctime=:acctime", Accept.class)
                .setParameter("acctime", acctime)
                .getResultList();
    }

    public List<Accept> findByStatus(AcceptStatus status){
        return em.createQuery("select a from Accept a where a.status=:status", Accept.class)
                .setParameter("status", status)
                .getResultList();
    }

    public List<Accept> findAll(){
        return em.createQuery("select a from Accept a", Accept.class).getResultList();
    }
}
