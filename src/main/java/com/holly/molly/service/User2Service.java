package com.holly.molly.service;

import com.holly.molly.domain.User1;
import com.holly.molly.domain.User2;
import com.holly.molly.repository.User1Repository;
import com.holly.molly.repository.User2Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class User2Service {
    private final User2Repository user2Repository;

    @Transactional//필요시에만 readOnly=false
    public Long join(User2 user2){
        validateDuplicateMember(user2);//중복회원 검증
        user2Repository.save(user2);
        return user2.getId();
    }

    private void validateDuplicateMember(User2 user2) {
    }

    public User2 findOne(Long memberId){
        return user2Repository.findOne(memberId);
    }

    public List<User2> findByName(String name){ return user2Repository.findByName(name); }


    public List<User2> findMembers() {
        return user2Repository.findAll();
    }
}
