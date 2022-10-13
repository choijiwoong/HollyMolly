package com.holly.molly.repository;

import com.holly.molly.domain.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;//for assertThat


public class MemoryMememberRepositoryTest {
    MemoryMemberRepository repository=new MemoryMemberRepository();

    @AfterEach
    public void afterEach(){
        repository.clearDB();//순서와 상관없게 하기 위함.(의존관계 X)
    }

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
        Member member1=new Member();
        member1.setEmailAddress("spring1@naver.com");
        repository.save(member1);

        Member member2=new Member();
        member2.setEmailAddress("spring2@naver.com");
        repository.save(member2);

        List<Member> result=repository.findAll();
        assertThat(result.size()).isEqualTo(2);
    }


}
