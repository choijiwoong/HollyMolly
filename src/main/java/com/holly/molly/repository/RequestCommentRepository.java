package com.holly.molly.repository;

import com.holly.molly.domain.Request;
import com.holly.molly.domain.RequestComment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RequestCommentRepository {
    private final EntityManager em;

    public void save(RequestComment requestComment){ em.persist(requestComment); }

    public void delete(RequestComment requestComment){ em.remove(requestComment); }

    public RequestComment findOne(Long id){ return em.find(RequestComment.class, id); }

    public List<RequestComment> findAll(){
        return em.createQuery("select c from RequestComment c", RequestComment.class).getResultList();
    }

    public List<RequestComment> findByRequest(Request request){
        return em.createQuery("select rc from RequestComment rc where rc.request.id=:id")
                .setParameter("id", request.getId())
                .getResultList();
    }

    public void clear(){ em.clear(); }//for test code
}
