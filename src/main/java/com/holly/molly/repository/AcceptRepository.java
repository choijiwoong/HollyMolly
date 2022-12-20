package com.holly.molly.repository;

import com.holly.molly.domain.*;
import static com.holly.molly.domain.QAccept.accept;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class AcceptRepository {
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public AcceptRepository(EntityManager em){
        this.em=em;
        this.queryFactory=new JPAQueryFactory(em);
    }

    public void save(Accept accept){ em.persist(accept); }
    public void delete(Accept accept){ em.remove(accept); }

    public Accept findOne(Long id){ return em.find(Accept.class, id); }

    public List<Accept> findByStatus(AcceptStatus status){
        return queryFactory.selectFrom(accept).where(accept.status.eq(status)).fetch();
    }

    public List<Accept> findByUser(User user){
        return queryFactory.selectFrom(accept).where(accept.userA.id.eq(user.getId())).fetch();
    }

    public List<Accept> findAll(){
        return queryFactory.selectFrom(accept).fetch();
    }
}
