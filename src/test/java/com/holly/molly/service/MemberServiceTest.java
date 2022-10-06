package com.holly.molly.service;

import com.holly.molly.domain.Member;
import com.holly.molly.repository.MemoryMemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MemberServiceTest {

    MemberService memberService;
    MemoryMemberRepository memberRepository;

    @BeforeEach
    public void beforeEach(){
        memberRepository=new MemoryMemberRepository();//static 문제 해결. test시 독립적인 repo와 serv생성.
        memberService=new MemberService(memberRepository);
    }

    @AfterEach
    public void afterEach(){
        memberRepository.clearDB();
    }

    @Test
    void 회원가입() {
        //given
        Member member=new Member();
        member.setEmailAddress("test@gmail.com");

        //when
        Long saveId=memberService.register(member);

        //then
        Member findMember=memberService.findOne(saveId).get();
        assertThat(member.getName()).isEqualTo(findMember.getName());
    }

    @Test
    public void 중복_회원_예외(){
        //given
        Member member1=new Member();
        member1.setEmailAddress("spring@gmail.com");

        Member member2=new Member();
        member2.setEmailAddress("spring@gmail.com");//같은 이메일의 회원 생성

        //when
        memberService.register(member1);
        IllegalStateException e=assertThrows(IllegalStateException.class, ()->memberService.register(member2));//예외가 터져야한다는 단정
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
        //then
    }

    @Test
    void findMembers() {
    }

    @Test
    void findOne() {
    }
}