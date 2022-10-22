package com.holly.molly.repository;

import com.holly.molly.domain.User1;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class User1Repository {

    @PersistenceContext
    private EntityManager em;

    public void save(User1 user){
        em.persist(user);
    }

    public User1 findOne(Long id){
        return em.find(User1.class, id);
    }

    public List<User1> findAll(){
        return em.createQuery("select u1 from User1 u1", User1.class).getResultList();
    }

    public List<User1> findByEmail(String email){
        return em.createQuery("select u1 from User1 u1 where u1.email=:email", User1.class)
                .setParameter("email", email)
                .getResultList();
    }
}
