package com.holly.molly.service;

import com.holly.molly.DTO.LoginDTO;
import com.holly.molly.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class UserServiceTest {
    @Autowired UserService userService;
    @Autowired
    EntityManager em;

    @AfterEach
    public void afterEach(){
        em.clear();
    }

    @Test
    void join() {
        //given
        User user1=new User("user1","user@gmail.com","1234","010-0000-0000","000000-0000000");
        userService.join(user1);

        User user2=new User("user2", "1user@gmail.com", "1234", "100-0000-0000", "000000-0000000");//pid중복

        User user3=new User("user3","user@gmail.com","1234","010-0010-0000","000100-0000000");

        User user4=new User("user4","us1er@gmail.com","1234","010-0000-0000","000010-0000000");

        //When
        IllegalStateException e2 = assertThrows(IllegalStateException.class,
                () -> userService.join(user2));//예외가 발생해야 한다.
        assertThat(e2.getMessage()).isEqualTo("주민번호가 중복된 회원입니다.");

        IllegalStateException e3 = assertThrows(IllegalStateException.class,
                () -> userService.join(user3));//예외가 발생해야 한다.
        assertThat(e3.getMessage()).isEqualTo("이메일이 중복된 회원입니다.");

        IllegalStateException e4 = assertThrows(IllegalStateException.class,
                () -> userService.join(user4));//예외가 발생해야 한다.
        assertThat(e4.getMessage()).isEqualTo("전화번호가 중복된 회원입니다.");
    }

    @Test
    void delete() {
        //given
        User user1=new User("user1","user@gmail.com","1234","010-0000-0000","000000-0000000");
        userService.join(user1);

        //when
        userService.delete(user1);

        //then
        User user2=new User("user1","user@gmail.com","1234","010-0000-0000","000000-0000000");//same infotmation
        userService.join(user2);
    }

    @Test
    void findOne() {
        //given
        User user1=new User("user1","user@gmail.com","1234","010-0000-0000","000000-0000000");
        userService.join(user1);

        //when
        assertEquals(userService.findOne(user1.getId()).isPresent(), true);
        assertEquals(userService.findOne(user1.getId()).get().getId(), user1.getId());
    }

    @Test
    void findByName() {
        //given
        User user1=new User("user1","user@gmail.com","1234","010-0000-0000","000000-0000000");
        userService.join(user1);

        //when
        assertEquals(userService.findByName(user1.getName()).size(), 1);
        assertEquals(userService.findByName(user1.getName()).stream().findFirst().get(), user1);
    }

    @Test
    void findAll() {
        //given
        User user1=new User("user1","user@gmail.com","1234","010-0000-0000","000000-0000000");
        userService.join(user1);

        //when
        assertEquals(userService.findAll().size(), 1);
        assertEquals(userService.findAll().stream().findFirst().get(), user1);
    }

    @Test
    void findByEmail() {
        //given
        User user1=new User("user1","user@gmail.com","1234","010-0000-0000","000000-0000000");
        userService.join(user1);

        //when
        assertEquals(userService.findByEmail(user1.getEmail()).isPresent(), true);
        assertEquals(userService.findByEmail(user1.getEmail()).stream().findFirst().get(), user1);
    }

    @Test
    void signUp() {
        //given1(등록되지 않은 회원의 정보로 로그인 하려는 경우)
        User user1=new User("user1","user@gmail.com","1234","010-0000-0000","000000-0000000");

        LoginDTO loginDTO=new LoginDTO();
        loginDTO.setEmail("user@gmail.com");
        loginDTO.setPassword("1234");
        //when1
        assertEquals(userService.signUp(loginDTO), Optional.empty());

        //given2(비밀번호가 다를 경우)
        userService.join(user1);
        loginDTO.setPassword("123");
        //when2
        assertEquals(userService.signUp(loginDTO), Optional.empty());

        //given3(이메일이 다를 경우)
        loginDTO.setEmail("ausak@gmail.com");
        loginDTO.setPassword("1234");
        //when3
        assertEquals(userService.signUp(loginDTO), Optional.empty());

        //given4(정확한 시도인 경우)
        loginDTO.setEmail("user@gmail.com");
        //when4
        assertEquals(userService.signUp(loginDTO).get(), user1);
    }
}