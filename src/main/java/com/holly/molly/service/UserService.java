package com.holly.molly.service;

import com.holly.molly.DTO.LocationDTO;
import com.holly.molly.DTO.LoginDTO;
import com.holly.molly.domain.User;
import com.holly.molly.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public Long join(User user){
        validateDuplicateMember(user);//중복회원 검증(이메일, 전화번호, 주민번호)
        userRepository.save(user);//DB저장
        return user.getId();
    }

    @Transactional
    public Long delete(User user){
        if(Optional.ofNullable(userRepository.findOne(user.getId())).isEmpty()){//탐색
            throw new RuntimeException("삭제하려는 User가 존재하지 않습니다.");
        }
        userRepository.delete(user);//DB삭제
        return user.getId();
    }

    public Optional<User> findOne(Long memberId){
        return Optional.ofNullable(userRepository.findOne(memberId));
    }

    public List<User> findByName(String name){ return userRepository.findByName(name); }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findByEmail(String email){ return Optional.ofNullable(userRepository.findByEmail(email)); }

    public Optional<User> signUp(LoginDTO loginDTO) {//회원가입..?로그인로직같은데
        return this.findByEmail(loginDTO.getEmail())
                .filter(u->u.getPassword().equals(loginDTO.getPassword()));
    }

    //----내부로직----
    private void validateDuplicateMember(User user) {//중복회원 검증
        if(Optional.ofNullable(userRepository.findByEmail(user.getEmail())).isPresent())
            throw new IllegalStateException("이메일이 중복된 회원입니다.");

        if(Optional.ofNullable(userRepository.findByPhone(user.getPhone())).isPresent())
            throw new IllegalStateException("전화번호가 중복된 회원입니다.");

        if(Optional.ofNullable(userRepository.findByPid(user.getPid())).isPresent()) {
            throw new IllegalStateException("주민번호가 중복된 회원입니다.");
        }
        return;
    }

    //-----서비스로직------
    public User parseUserCookie(Cookie cookie) {//쿠키를 Optional<User>로 변환
        Optional<User> userInfo = this.findOne(Long.valueOf(cookie.getValue()));
        if (userInfo.isEmpty()) {
            throw new RuntimeException("cannot find current user information on cookie");
        }
        return userInfo.get();
    }

    public Cookie createCookie(Long id){//userId 쿠키생성
        Cookie idCookie=new Cookie("userId", id==null?null:String.valueOf(id));
        idCookie.setPath("/");
        return idCookie;
    }

    public Cookie createCookieLat(String latitude){//user latitude쿠키생성
        Cookie idCookie=new Cookie("latitude", latitude);
        idCookie.setPath("/");
        return idCookie;
    }

    public Cookie createCookieLng(String longitude){//user longitude쿠키생성
        Cookie idCookie=new Cookie("longitude", longitude);
        idCookie.setPath("/");
        return idCookie;
    }

}
