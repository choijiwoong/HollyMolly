package com.holly.molly.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Request {
    @Id
    @GeneratedValue
    @Column(name="request_id")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="userR_id")
    private User userR;

    private LocalDateTime reqtime;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    private LocalDateTime exectime;

    private String address;

    private String content;

    @OneToOne(fetch =FetchType.LAZY)
    private Accept accept;

    //---연관관계 메서드---
    public void setUserR(User user){
        this.userR=user;
        user.getRequests().add(this);//유저에 변경사항 적용
    }
}
