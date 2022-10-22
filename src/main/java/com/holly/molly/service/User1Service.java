package com.holly.molly.service;

import com.holly.molly.domain.User1;
import com.holly.molly.repository.User1Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class User1Service {
    private final User1Repository user1Repository;

    @Transactional//필요시에만 readOnly=false
    public Long join(User1 user1){
        validateDuplicateMember(user1);//중복회원 검증
        user1Repository.save(user1);
        return user1.getId();
    }

    private void validateDuplicateMember(User1 user1) {
        try {
            User1 findMembers1 = user1Repository.findByEmail(user1.getName());
            throw new IllegalStateException("이미 등록된 이메일입니다.");
        } catch(Exception e1){
            User1 findMembers2 = user1Repository.findByPhone(user1.getPhone());
            User1 findMembers3 = user1Repository.findByPid(user1.getPid());
        }
    }

    public User1 findOne(Long memberId){
        return user1Repository.findOne(memberId);
    }

    public List<User1> findByName(String name){ return user1Repository.findByName(name); }


    public List<User1> findMembers() {
        return user1Repository.findAll();
    }
}
