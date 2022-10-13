package com.holly.molly.service;

import com.holly.molly.controller.LoginForm;
import com.holly.molly.domain.Member;
import com.holly.molly.repository.MemberRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

//@Service//@Component in Repo, Controller..모든 component는 자바빈등록, 그 뒤 각각이 DI. 이러한 방식을 컴포넌트 스캔(자동)으로 자바빈에 등록한다고 한다.
@Transactional
public class MemberService {
    private static Boolean isAuthenticated=false;
    private final MemberRepository memberRepository;
    //@Autowired//controller->service->repo의존성이기에 repo연결을 위해 service에서도 autowired를 걸어준다. DI
    public MemberService(MemberRepository memberRepository){//직접 생성이 아닌 외부에서 넣어주게끔 하여 공통 repository사용케
        this.memberRepository=memberRepository;
    }

    /*
    *회원가입
     */
    public Long register(Member member){//회원가입
        valiidateDuplicateMember(member);//중복회원 검증(email)
        memberRepository.save(member);
        return member.getInnerId();
    }

    private void valiidateDuplicateMember(Member member) {
        memberRepository.findByEmail(member.getEmailAddress())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    /*
    * 전체 회원 조회
    */
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    public Optional<Member> findOne(Long innerId){
        return memberRepository.findById(innerId);
    }
    public Optional<Member> findOne(String emailAddress){ return memberRepository.findByEmail(emailAddress); }

    public Optional<Member> signUp(LoginForm form) {
        return this.findOne(form.getEmailAddress());
    }
}
