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
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="userA_id")
    private User userA;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AcceptStatus status;

    @OneToOne(mappedBy="accept", fetch=FetchType.LAZY)
    private Request request;

    public Accept(User user, Request request){
        if(request.getUserR().getId().equals(user.getId()))
            throw new RuntimeException("봉사신청자와 봉사자 정보가 동일합니다!");

        this.connectUser(user);
        this.status=AcceptStatus.REGISTER;
        this.connectRequest(request);
    }

    //---연관관계 메서드---
    public void connectUser(User user){
        this.userA=user;
        user.getAccepts().add(this);
    }

    public void connectRequest(Request request){
        request.setAccept(this);
        request.changeStatus(RequestStatus.ACCEPT);
        this.request=request;
    }

    public void changeStatus(AcceptStatus status){
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
            //throw new RuntimeException("Incorrect handling");
            this.status=AcceptStatus.REGISTER;
        }

        if(status.equals(AcceptStatus.REVIEWD)){
            this.status=AcceptStatus.REVIEWD;
        }
    }
}