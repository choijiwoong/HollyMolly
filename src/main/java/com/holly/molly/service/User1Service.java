package com.holly.molly.service;

import com.holly.molly.controller.LoginDTO;
import com.holly.molly.domain.User1;
import com.holly.molly.repository.User1Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    }

    public User1 findOne(Long memberId){
        return user1Repository.findOne(memberId);
    }

    public List<User1> findByName(String name){ return user1Repository.findByName(name); }

    public List<User1> findMembers() {
        return user1Repository.findAll();
    }

    public List<User1> findByEmail(String email){ return user1Repository.findByEmail(email); }

    public Optional<User1> signUp(LoginDTO loginDTO) {
        return this.findByEmail(loginDTO.getEmail())
                .stream()
                .filter(u->u.getPassword()
                        .equals(loginDTO.getPassword()))
                .findFirst();
    }
}
