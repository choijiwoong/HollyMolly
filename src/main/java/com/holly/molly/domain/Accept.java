package com.holly.molly.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Accept{
    @Id
    @GeneratedValue
    @Column(name="accept_id")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="userA_id")
    private User userA;

    @OneToOne(fetch = FetchType.LAZY)
    private Volun volunA;

    private LocalDateTime acctime;

    @Enumerated(EnumType.STRING)
    private AcceptStatus status;

    //---연관관계 메서드---
    //public void setUser(User user){
    //    this.userA=user;
    //    user.getAccepts().add(this);
    //}

    //public void setVolun(Volun volun){
    //    this.volunA=volun;
    //    volun.setAccept(this);
    //}

    //---생성 메서드---
    public static Accept createAccept(User user, Volun volun){
        Accept accept=new Accept();
        accept.setUserA(user);
        accept.setVolunA(volun);

        accept.setAcctime(LocalDateTime.now());
        accept.setStatus(AcceptStatus.REGISTER);

        return accept;
    }

    public static Accept createNull(){
        return new Accept();
    }
}