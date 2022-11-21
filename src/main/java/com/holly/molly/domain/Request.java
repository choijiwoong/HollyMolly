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

    //private String location;
    private double latitude, longitude;

    private String content;

    @OneToOne(fetch =FetchType.LAZY)
    private Accept accept;

    //---연관관계 메서드---
    //public void setUser(User user){
    //    this.userR=user;
    //    user.getRequests().add(this);
    //}

    //public void setVolun(Volun volun){
    //    this.volunR=volun;
    //    volun.setRequest(this);
    //}

    //---생성 메서드---
    public static Request createRequest(User user){
        Request request=new Request();
        request.setUserR(user);

        request.setReqtime(LocalDateTime.now());
        request.setStatus(RequestStatus.REGISTER);

        return request;
    }
}
