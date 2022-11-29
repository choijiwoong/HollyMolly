package com.holly.molly.repository;

import com.holly.molly.domain.Request;
import com.holly.molly.domain.RequestStatus;
import com.holly.molly.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RequestRepository {
    private final EntityManager em;

    public void save(Request request){
        em.persist(request);
    }

    public void delete(Request request){ em.remove(request); }

    public Request findOne(Long id){
        return em.find(Request.class, id);
    }

    public List<Request> findByUser(User user){
        return em.createQuery("select r from Request r where r.userR.id=:id", Request.class)
                .setParameter("id", user.getId())
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

    public void clear(){ em.clear(); }//for test code
}
