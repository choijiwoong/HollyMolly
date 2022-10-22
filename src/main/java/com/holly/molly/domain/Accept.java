package com.holly.molly.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="accepts")
@Getter
@Setter
public class Accept{
    @Id
    @GeneratedValue
    @Column(name="accept_id")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user2_id")
    private User2 user;

    @OneToOne(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
    private Volun volun;

    private LocalDateTime acctime;

    @Enumerated(EnumType.STRING)
    private AcceptStatus status;

    //---연관관계 메서드---
    public void setUser2(User2 user){
        this.user=user;
        user.getAccepts().add(this);
    }

    public void setVolun(Volun volun){
        this.volun=volun;
        volun.setAccept(this);
    }

    //---생성 메서드---
    public static Accept createRequest(User2 user, Volun volun){
        Accept accept=new Accept();
        accept.setUser2(user);
        accept.setVolun(volun);

        accept.setAcctime(LocalDateTime.now());
        accept.setStatus(AcceptStatus.REGISTER);

        return accept;
    }
}