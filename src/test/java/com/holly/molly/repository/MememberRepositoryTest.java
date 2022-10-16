package com.holly.molly.repository;

import com.holly.molly.domain.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;//for assertThat


@SpringBootTest
@Transactional
public class MememberRepositoryTest {
    @Autowired
    MemberRepository repository;


    @Test
    public void save(){
        Member member=new Member();
        member.setEmailAddress("choijiwoong@gmail.com");

        repository.save(member);
        Member result=repository.findByEmail(member.getEmailAddress()).get();
        assertThat(member).isEqualTo(result);
    }

    @Test
    public void findByEmail(){
        Member member1=new Member();
        member1.setEmailAddress("spring1@naver.com");
        repository.save(member1);

        Member member2=new Member();
        member2.setEmailAddress("spring2@naver.com");
        repository.save(member2);

        Member result=repository.findByEmail("spring1@naver.com").get();
        assertThat(member1).isEqualTo(result);
    }

    @Test
    public void findAll(){
        Long initSize=repository.count();
        Member member1=new Member();
        member1.setEmailAddress("spring1@naver.com");
        repository.save(member1);

        Member member2=new Member();
        member2.setEmailAddress("spring2@naver.com");
        repository.save(member2);

        List<Member> result=repository.findAll();
        assertThat(result.size()).isEqualTo(initSize+2);
    }
}
