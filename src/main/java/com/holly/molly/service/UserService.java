package com.holly.molly.service;

import com.holly.molly.controller.LoginDTO;
import com.holly.molly.domain.User;
import com.holly.molly.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional//필요시에만 readOnly=false
    public Long join(User user){
        validateDuplicateMember(user);//중복회원 검증
        userRepository.save(user);
        return user.getId();
    }

    @Transactional
    public Long delete(User user){
        userRepository.delete(user);
        return user.getId();
    }

    private void validateDuplicateMember(User user) {
        if(!userRepository.findByEmail(user.getEmail()).isEmpty())
            throw new RuntimeException("중복된 회원입니다.");

        if(!userRepository.findByPhone(user.getPhone()).isEmpty())
            throw new RuntimeException("중복된 회원입니다.");

        if(!userRepository.findByPid(user.getPid()).isEmpty())
            throw new RuntimeException("중복된 회원입니다.");
        return;
    }

    public User findOne(Long memberId){
        return userRepository.findOne(memberId);
    }

    public List<User> findByName(String name){ return userRepository.findByName(name); }

    public List<User> findMembers() {
        return userRepository.findAll();
    }

    public List<User> findByEmail(String email){ return userRepository.findByEmail(email); }

    public Optional<User> signUp(LoginDTO loginDTO) {
        return this.findByEmail(loginDTO.getEmail())
                .stream()
                .filter(u->u.getPassword()
                        .equals(loginDTO.getPassword()))
                .findFirst();
    }
}
