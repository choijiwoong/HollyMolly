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
        List<User1> findMembers=user1Repository.findByEmail(user1.getName());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    public List<User1> findMembers(){
        return user1Repository.findAll();
    }

    public User1 findOne(Long memberId){
        return user1Repository.findOne(memberId);
    }

}
