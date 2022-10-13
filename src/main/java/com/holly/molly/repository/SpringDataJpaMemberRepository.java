package com.holly.molly.repository;

import com.holly.molly.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//SpringConfig에 직접 repository를 설정하지 않아도 아래 인터페이스는 org.springframework.data.jpa.repository.JpaRepository를 포함하기에 알아서 Bean에 등록된다.(상속받은 JpaRepository != JpaMemberRepository라는 점 유의)
public interface SpringDataJpaMemberRepository extends JpaRepository<Member, Long> , MemberRepository{//알맹이, 껍데기를 동시에 상속

    Optional<Member> findByEmail(String name);
}
