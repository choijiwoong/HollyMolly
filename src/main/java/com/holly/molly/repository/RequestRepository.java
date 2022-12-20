package com.holly.molly.repository;

import com.holly.molly.domain.QRequest;
import static com.holly.molly.domain.QRequest.request;
import com.holly.molly.domain.Request;
import com.holly.molly.domain.RequestStatus;
import com.holly.molly.domain.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class RequestRepository {
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public RequestRepository(EntityManager em){
        this.em=em;
        this.queryFactory=new JPAQueryFactory(em);
    }

    public void save(Request request){
        em.persist(request);
    }

    public void delete(Request request){ em.remove(request); }

    public Request findOne(Long id){
        return em.find(Request.class, id);
    }

    public List<Request> findByUser(User user){
        return queryFactory.selectFrom(request).where(request.userR.id.eq(user.getId())).fetch();
    }

    public List<Request> findByStatus(RequestStatus status){
        return queryFactory.selectFrom(request).where(request.status.eq(status)).fetch();
    }

    public List<Request> findAll(){
        return queryFactory.selectFrom(request).fetch();
    }
}
