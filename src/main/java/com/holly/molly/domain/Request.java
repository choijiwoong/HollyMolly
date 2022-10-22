package com.holly.molly.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name="requests")
@Getter
@Setter
public class Request {
    @Id
    @GeneratedValue
    @Column(name="request_id")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user1_id")
    private User1 user;

    @OneToOne(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
    private Volun volun;

    private LocalDateTime reqtime;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    //---연관관계 메서드---
    public void setUser1(User1 user){
        this.user=user;
        user.getRequests().add(this);
    }

    public void setVolun(Volun volun){
        this.volun=volun;
        volun.setRequest(this);
    }

    //---생성 메서드---
    public static Request createRequest(User1 user, Volun volun){
        Request request=new Request();
        request.setUser1(user);
        request.setVolun(volun);

        request.setReqtime(LocalDateTime.now());
        request.setStatus(RequestStatus.REGISTER);

        return request;
    }
}
