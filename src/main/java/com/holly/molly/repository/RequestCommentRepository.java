package com.holly.molly.repository;

import com.holly.molly.domain.QRequestComment;
import static com.holly.molly.domain.QRequestComment.requestComment;
import com.holly.molly.domain.Request;
import com.holly.molly.domain.RequestComment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class RequestCommentRepository {
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public RequestCommentRepository(EntityManager em){
        this.em=em;
        this.queryFactory=new JPAQueryFactory(em);
    }

    public void save(RequestComment requestComment){ em.persist(requestComment); }

    public void delete(RequestComment requestComment){ em.remove(requestComment); }

    public RequestComment findOne(Long id){ return em.find(RequestComment.class, id); }

    public List<RequestComment> findAll(){
        return queryFactory.selectFrom(requestComment).fetch();
    }

    public List<RequestComment> findByRequest(Request request){
        return queryFactory.selectFrom(requestComment).where(requestComment.request.id.eq(request.getId())).fetch();
    }
}