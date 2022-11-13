package com.holly.molly.repository;

import com.holly.molly.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserRepositoryTest {
    @Autowired UserRepository userRepository;

    @BeforeEach
    public void clear(){
        userRepository.clear();
    }

    @Test
    void save() {
        //User user=new User();
        //user.setName("zonpark");
        //user.setBirth("2000.01.01");
        //user.setEmail("zonpark@gmail.com");
        //user.setPassword("1234");
        //user.setPhone("010-0000-0000");

        //userRepository.save(user);

        //List<User> members=userRepository.findAll();
        //assertThat(!members.isEmpty());//비어있는지 확인

        //assertThat(members.stream().findFirst().get().getEmail().equals("zonpark@gmail.com"));
        //assertThat(members.stream().findFirst().get().getPhone().equals("010-0000-0000"));
    }

    @Test
    void findOne() {
    }

    @Test
    void findByName() {
    }

    @Test
    void findByEmail() {
    }

    @Test
    void findByPhone() {
    }

    @Test
    void findByBirth() {
    }

    @Test
    void findByPid() {
    }

    @Test
    void findAll() {
    }
}