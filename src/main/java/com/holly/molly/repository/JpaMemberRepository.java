package com.holly.molly.repository;

import com.holly.molly.domain.Member;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
//JPA는 항상(정보를 저장할때 ex register) 트랜젝션이 있어야 하기에 Service에 @Transectional을 붙인다.
public class JpaMemberRepository implements MemberRepository{//엔티티 사용시 엔티티 매니저가 내부적으로 모든 로직 수행

    private final EntityManager em;//자동으로 JPA, DB등과 연결해서 JPA가 알아서 EM을 통해 필요업무를 수행한다.

    public JpaMemberRepository(EntityManager em){
        this.em=em;
    }

    @Override
    public Member save(Member member) {
        em.persist(member);//알아서 member변수를 이용하여 SQL만들고 수행
        return member;
    }

    @Override
    public Optional<Member> findById(Long innerId) {
        Member member=em.find(Member.class, innerId);
        return Optional.ofNullable(member);
    }

    @Override
    public Optional<Member> findByEmail(String emailAddress) {
        List<Member> result=em.createQuery("select m from Member m where m.emailAddress = :emailAddress", Member.class)//쿼리 치환될 곳을 :로 네이민
                .setParameter("emailAddress", emailAddress)//해당 네이밍된 곳에 인자를 대입
                .getResultList();//탐색
        return result.stream().findAny();//아무거나 먼저 찾은거 리턴
    }

    @Override
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)//JPQL 엔티티대상 쿼리. 검색에 객체 자체를 사용
                .getResultList();
    }
}
