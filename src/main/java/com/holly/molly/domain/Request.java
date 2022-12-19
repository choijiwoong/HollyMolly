package com.holly.molly.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.bytebuddy.asm.Advice;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @OneToMany(mappedBy = "request")//자기 맴버변수 이름을 참조
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
        user.getRequests().add(this);//유저에 변경사항 적용
    }
}
