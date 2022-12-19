package com.holly.molly.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Request {
    @Id
    @GeneratedValue
    @Column(name="request_id")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="userR_id")
    private User userR;

    @Column(nullable = false)
    private LocalDateTime reqtime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @Column(nullable = false)@Setter//test for making situation
    private LocalDateTime exectime;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String content;

    @OneToOne(fetch =FetchType.LAZY)
    private Accept accept;

    @OneToMany(mappedBy = "request")
    private List<RequestComment> comments=new ArrayList<>();

    public Request(User user, LocalDateTime exectime, String address, String content){
        this.connectUser(user);
        this.reqtime=LocalDateTime.now();
        this.status=RequestStatus.REGISTER;
        this.exectime=exectime;
        this.address=address;
        this.content=content;
    }

    //---연관관계 메서드---
    public void connectUser(User user){
        this.userR=user;
        user.getRequests().add(this);
    }

    public void setAccept(Accept accept){
        this.accept=accept;
    }

    public void changeStatus(RequestStatus status){
        this.status=status;
    }
}
