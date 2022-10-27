package com.holly.molly.repository;

import com.holly.molly.domain.User1;
import com.holly.molly.domain.User2;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class User2Repository {

    @PersistenceContext
    private EntityManager em;

    public void save(User2 user){
        em.persist(user);
    }

    public User2 findOne(Long id){
        return em.find(User2.class, id);
    }

    public List<User2> findByName(String name){
        return em.createQuery("select u2 from User2 u2 where u2.name=:name", User2.class)
                .setParameter("name", name)
                .getResultList();
    }

    public List<User2> findByEmail(String email){
        return em.createQuery("select u2 from User2 u2 where u2.email=:email", User2.class)
                .setParameter("email", email)
                .getResultList();
    }

    public List<User2> findByPhone(String phone){
        return em.createQuery("select u2 from User2 u2 where u2.phone=:phone", User2.class)
                .setParameter("phone", phone)
                .getResultList();
    }

    public List<User2> findByBirth(String birth){
        return em.createQuery("select u2 from User2 u2 where u2.birth=:birth", User2.class)
                .setParameter("birth", birth)
                .getResultList();
    }

    public List<User2> findByPid(String pid){
        return em.createQuery("select u2 from User2 u2 where u2.pid=:pid", User2.class)
                .setParameter("pid", pid)
                .getResultList();
    }

    public List<User2> findAll(){
        return em.createQuery("select u2 from User2 u2", User2.class).getResultList();
    }
}