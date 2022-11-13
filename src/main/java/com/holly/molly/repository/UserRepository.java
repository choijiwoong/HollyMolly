package com.holly.molly.repository;

import com.holly.molly.domain.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserRepository {
    @PersistenceContext
    private EntityManager em;

    public void save(User user){
        em.persist(user);
    }

    public User findOne(Long id){
        return em.find(User.class, id);
    }

    public List<User> findByName(String name){
        return em.createQuery("select u from User u where u.name=:name", User.class)
                .setParameter("name", name)
                .getResultList();
    }

    public List<User> findByEmail(String email){
        return em.createQuery("select u from User u where u.email=:email", User.class)
                .setParameter("email", email)
                .getResultList();
    }

    public List<User> findByPhone(String phone){
        return em.createQuery("select u from User u where u.phone=:phone", User.class)
                .setParameter("phone", phone)
                .getResultList();
    }

    public List<User> findByBirth(String birth){
        return em.createQuery("select u from User u where u.birth=:birth", User.class)
                .setParameter("birth", birth)
                .getResultList();
    }

    public List<User> findByPid(String pid){
        return em.createQuery("select u from User u where u.pid=:pid", User.class)
                .setParameter("pid", pid)
                .getResultList();
    }

    public List<User> findAll(){
        return em.createQuery("select u from User u", User.class).getResultList();
    }

    public void clear(){
        em.clear();
    }
}
