package com.holly.molly.repository;

import com.holly.molly.domain.QUser;
import static com.holly.molly.domain.QUser.user;
import com.holly.molly.domain.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public UserRepository(EntityManager em){
        this.em=em;
        this.queryFactory=new JPAQueryFactory(em);//em에 의존하기에 동시성 문제X
    }

    public void save(User user){
        em.persist(user);
    }
    public void delete(User user){ em.remove(user); }

    public User findOne(Long id){
        return em.find(User.class, id);
    }

    public List<User> findByName(String name){
        return queryFactory.selectFrom(user).where(user.name.eq(name)).fetch();
    }

    public User findByEmail(String email){
        return queryFactory.selectFrom(user).where(user.email.eq(email)).fetchOne();
    }

    public User findByPhone(String phone){
        return queryFactory.selectFrom(user).where(user.phone.eq(phone)).fetchOne();
    }

    public User findByPid(String pid){
        return queryFactory.selectFrom(user).where(user.pid.eq(pid)).fetchOne();
    }

    public List<User> findAll(){
        return queryFactory.selectFrom(user).fetch();
    }
}
