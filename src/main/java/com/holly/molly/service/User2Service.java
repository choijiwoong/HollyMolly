package com.holly.molly.service;

import com.holly.molly.controller.LoginDTO;
import com.holly.molly.domain.User1;
import com.holly.molly.domain.User2;
import com.holly.molly.repository.User1Repository;
import com.holly.molly.repository.User2Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
        if(!user2Repository.findByEmail(user2.getEmail()).isEmpty())
            throw new RuntimeException("중복된 회원입니다.");

        if(!user2Repository.findByPhone(user2.getPhone()).isEmpty())
            throw new RuntimeException("중복된 회원입니다.");

        if(!user2Repository.findByPid(user2.getPid()).isEmpty())
            throw new RuntimeException("중복된 회원입니다.");
        return;
    }

    public User2 findOne(Long memberId){
        return user2Repository.findOne(memberId);
    }

    public List<User2> findByName(String name){ return user2Repository.findByName(name); }

    public List<User2> findMembers() {
        return user2Repository.findAll();
    }

    public List<User2> findByEmail(String email){ return user2Repository.findByEmail(email); }

    public Optional<User2> signUp(LoginDTO loginDTO) {
        return this.findByEmail(loginDTO.getEmail())
                .stream()
                .filter(u->u.getPassword()
                        .equals(loginDTO.getPassword()))
                .findFirst();
    }
}
