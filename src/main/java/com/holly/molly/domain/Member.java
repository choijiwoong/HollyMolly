package com.holly.molly.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity //JPA가 관리하는 요소를 의미
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)//DB가 직접 ㄱ생성해줌을 의미
    private Long innerId;//내부적인 인원 번호. primary key
    private String name;//사용자 이름(닉네임 가능)
    private String email;//아이디처럼 사용 예정
    private String password;//이메일 별 비밀번호

    public Long getInnerId(){
        return innerId;
    }
    public void setInnerId(Long innerId){
        this.innerId=innerId;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }

    public String getEmailAddress(){
        return email;
    }
    public void setEmailAddress(String emailAddress){
        this.email=emailAddress;
    }

    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password=password;
    }

}
