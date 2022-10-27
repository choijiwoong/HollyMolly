package com.holly.molly.repository;

import com.holly.molly.domain.Request;
import com.holly.molly.domain.RequestStatus;
import com.holly.molly.domain.User1;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

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

    public List<Request> findByReqtime(LocalDateTime reqtime){
        return em.createQuery("select r from Request r where r.reqtime=:reqtime", Request.class)
                .setParameter("reqtime", reqtime)
                .getResultList();
    }

    public List<Request> findByStatus(RequestStatus status){
        return em.createQuery("select r from Request r where r.status=:status", Request.class)
                .setParameter("status", status)
                .getResultList();
    }

    public List<Request> findAll(){
        return em.createQuery("select r from Request r", Request.class).getResultList();
    }
}
