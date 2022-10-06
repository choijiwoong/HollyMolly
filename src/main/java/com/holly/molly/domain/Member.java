package com.holly.molly.domain;

import lombok.Getter;
import lombok.Setter;

public class Member {
    private Long innerId;//내부적인 인원 번호. primary key
    private String name;//사용자 이름(닉네임 가능)
    private String emailAddress;//아이디처럼 사용 예정
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
        return emailAddress;
    }
    public void setEmailAddress(String emailAddress){
        this.emailAddress=emailAddress;
    }

    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password=password;
    }

}
