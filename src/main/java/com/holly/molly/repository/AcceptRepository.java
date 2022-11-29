package com.holly.molly.repository;

import com.holly.molly.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AcceptRepository {
    private final EntityManager em;

    public void save(Accept accept){ em.persist(accept); }
    public void delete(Accept accept){ em.remove(accept); }

    public Accept findOne(Long id){ return em.find(Accept.class, id); }

    public List<Accept> findByStatus(AcceptStatus status){
        return em.createQuery("select a from Accept a where a.status=:status", Accept.class)
                .setParameter("status", status)
                .getResultList();
    }

    public List<Accept> findByUser(User user){
        return em.createQuery("select a from Accept a where a.userA.id=:id", Accept.class)
                .setParameter("id", user.getId())
                .getResultList();
    }

    public List<Accept> findAll(){
        return em.createQuery("select a from Accept a", Accept.class).getResultList();
    }

    public void clear(){ em.clear(); }//for test code
}
