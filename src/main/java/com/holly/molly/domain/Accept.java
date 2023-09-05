package com.holly.molly.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Accept extends JpaBaseEntity{
    @Id
    @GeneratedValue
    @Column(name="accept_id")
    private Long id;//내부적으로 사용할 unique key

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="userA_id")
    private User userA;//수락한 유저정보

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AcceptStatus status;//accept의 상태정보

    @OneToOne(mappedBy="accept", fetch=FetchType.LAZY)
    private Request request;//request연결정보

    public Accept(User user, Request request){
        if(request.getUserR().getId().equals(user.getId()))
            throw new RuntimeException("봉사신청자와 봉사자 정보가 동일합니다!");

        this.connectUser(user);//accept와 유저 연결
        this.connectRequest(request);//request와 accept 연결 및 request의 상태변경

        request.changeStatus(RequestStatus.ACCEPT);//[DEBUG] Request의 상태를 먼저 조작. (전적으로 accept의 changeStatus에 상태변경을 위임하기에)
        this.changeStatus(AcceptStatus.REGISTER);//초기화
    }

    //---연관관계 메서드---
    public void connectUser(User user){//accept와 user연결
        this.userA=user;
        user.getAccepts().add(this);
    }

    public void connectRequest(Request request){//accept에 request연결
        this.request=request;
        request.setAccept(this);
    }

    public void changeStatus(AcceptStatus status){//accept와 request의 상태변경 제어
        if(status.equals(AcceptStatus.COMPLETE)){
            this.status=AcceptStatus.COMPLETE;

            this.request.changeStatus(RequestStatus.COMPLETE);
        }

        if(status.equals(AcceptStatus.CANCEL)){
            this.status=AcceptStatus.CANCEL;

            if(this.getRequest().getExectime().isBefore(LocalDateTime.now())){
                this.request.changeStatus(RequestStatus.CANCEL);
            } else{
                this.request.changeStatus(RequestStatus.REGISTER);
            }
        }

        if(status.equals(AcceptStatus.REGISTER)){
            this.status=AcceptStatus.REGISTER;
        }

        if(status.equals(AcceptStatus.REVIEWD)){
            this.status=AcceptStatus.REVIEWD;
        }

        if(status.equals(AcceptStatus.RUNNING)){
            this.status=AcceptStatus.RUNNING;
        }

        if(status.equals(AcceptStatus.EMERGENCY)){
            this.status=AcceptStatus.EMERGENCY;
        }
    }
}