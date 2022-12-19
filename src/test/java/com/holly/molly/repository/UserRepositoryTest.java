package com.holly.molly.repository;

import com.holly.molly.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserRepositoryTest {//em관련 메서드는 테스트서 제외
    @Autowired UserRepository userRepository;

    @AfterEach
    public void afterEach(){
        userRepository.clear();
    }

    @Test
    void findOne() {
        //given
        User user=new User("홍길동","hongil@gmail.com","1234","010-0000-0000","000000-0000000");
        userRepository.save(user);

        //when
        User user2=userRepository.findOne(user.getId());

        //then
        assertEquals(user2.getPid(), user.getPid());
    }

    @Test
    void findByName() {
        //given
        User user=new User("홍길동","hongil@gmail.com","1234","010-0000-0000","000000-0000000");
        userRepository.save(user);

        //when
        List<User> users=userRepository.findByName(user.getName());

        //then
        assertEquals(users.stream().findFirst().get().getId(), user.getId());
    }

    @Test
    void findByEmail() {
        //given
        User user=new User("홍길동","hongil@gmail.com","1234","010-0000-0000","000000-0000000");
        userRepository.save(user);

        //when
        List<User> users=userRepository.findByEmail(user.getEmail());

        //then
        assertEquals(users.stream().findFirst().get().getId(), user.getId());
    }

    @Test
    void findByPhone() {
        //given
        User user=new User("홍길동","hongil@gmail.com","1234","010-0000-0000","000000-0000000");
        userRepository.save(user);

        //when
        List<User> users=userRepository.findByPhone(user.getPhone());

        //then
        assertEquals(users.stream().findFirst().get().getId(), user.getId());
    }

    @Test
    void findByPid() {
        //given
        User user=new User("홍길동","hongil@gmail.com","1234","010-0000-0000","000000-0000000");
        userRepository.save(user);

        //when
        List<User> users=userRepository.findByPid(user.getPid());

        //then
        assertEquals(users.stream().findFirst().get().getId(), user.getId());
    }

    @Test
    void findAll() {
        //given
        User user=new User("홍길동","hongil@gmail.com","1234","010-0000-0000","000000-0000000");
        userRepository.save(user);

        //when
        List<User> users=userRepository.findAll();

        //then
        assertEquals(users.size(), 1l);
        assertEquals(users.stream().findFirst().get().getId(), user.getId());
    }
}