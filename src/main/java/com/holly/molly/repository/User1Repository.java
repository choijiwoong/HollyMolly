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

    public List<User1> findByName(String name){
        return em.createQuery("select u1 from User1 u1 where u1.name=:name", User1.class)
                .setParameter("name", name)
                .getResultList();
    }

    public User1 findByEmail(String email){
        return em.find(User1.class, email);
    }

    public User1 findByPhone(String phone){
        return em.find(User1.class, phone);
    }

    public List<User1> findByBirth(String birth){
        return em.createQuery("select u1 from User1 u1 where u1.birth=:birth", User1.class)
                .setParameter("birth", birth)
                .getResultList();
    }

    public User1 findByPid(String pid){
        return em.find(User1.class, pid);
    }

    public List<User1> findAll(){
        return em.createQuery("select u1 from User1 u1", User1.class).getResultList();
    }

}
