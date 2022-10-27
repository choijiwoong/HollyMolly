package com.holly.molly.repository;

import com.holly.molly.domain.Volun;
import com.holly.molly.domain.VolunStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class VolunRepository {
    private static EntityManager em;

    public void save(Volun volun){ em.persist(volun); }

    public Volun findOne(Long id){ return em.find(Volun.class, id); }

    public List<Volun> findByExectime(LocalDateTime exectime){
        return em.createQuery("select v from Volun v where v.exectime=:exectime", Volun.class)
                .setParameter("exectime", exectime)
                .getResultList();
    }

    public List<Volun> findByStatus(VolunStatus status){
        return em.createQuery("select v from Volun v where v.status=:status", Volun.class)
                .setParameter("status", status)
                .getResultList();
    }

    public List<Volun> findAll(){
        return em.createQuery("select v from Volun v", Volun.class).getResultList();
    }
}
