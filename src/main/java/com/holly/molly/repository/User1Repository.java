package com.holly.molly.repository;

import com.holly.molly.domain.User1;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<User1> findByEmail(String email){
        return em.createQuery("select u1 from User1 u1 where u1.email=:email", User1.class)
                .setParameter("email", email)
                .getResultList();
    }

    public List<User1> findByPhone(String phone){
        return em.createQuery("select u1 from User1 u1 where u1.phone=:name", User1.class)
                .setParameter("phone", phone)
                .getResultList();
    }

    public List<User1> findByBirth(String birth){
        return em.createQuery("select u1 from User1 u1 where u1.birth=:birth", User1.class)
                .setParameter("birth", birth)
                .getResultList();
    }

    public List<User1> findByPid(String pid){
        return em.createQuery("select u1 from User1 u1 where u1.pid=:pid", User1.class)
                .setParameter("pid", pid)
                .getResultList();
    }

    public List<User1> findAll(){
        return em.createQuery("select u1 from User1 u1", User1.class).getResultList();
    }

}
