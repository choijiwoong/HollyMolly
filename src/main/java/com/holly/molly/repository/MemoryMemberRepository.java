package com.holly.molly.repository;

import com.holly.molly.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.*;

//@Repository
public class MemoryMemberRepository implements MemberRepository{
    private static Map<Long, Member> memoryDB=new HashMap<>();//DB
    private static long sequence=0L;

    @Override
    public Member save(Member member) {
        member.setInnerId(++sequence);
        memoryDB.put(member.getInnerId(), member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(memoryDB.get(id));
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return memoryDB.values().stream()
                .filter(member->member.getEmailAddress().equals(email))
                .findAny();
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(memoryDB.values());
    }

    public void clearDB(){
        memoryDB.clear();
    }
}
