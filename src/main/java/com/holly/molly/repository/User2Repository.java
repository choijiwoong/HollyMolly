package com.holly.molly.repository;

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

    public List<User2> findAll(){
        return em.createQuery("select u2 from User2 u2", User2.class).getResultList();
    }

    public List<User2> findByEmail(String email){
        return em.createQuery("select u2 from User2 u2 where u2.email=:email", User2.class)
                .setParameter("email", email)
                .getResultList();
    }
}