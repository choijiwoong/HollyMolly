package com.holly.molly.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Accept{
    @Id
    @GeneratedValue
    @Column(name="accept_id")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="userA_id")
    private User userA;

    private LocalDateTime acctime;

    @Enumerated(EnumType.STRING)
    private AcceptStatus status;

    @OneToOne(mappedBy="accept", fetch=FetchType.LAZY)
    private Request request;

    public Accept(User user, Request request){
        this.connectUser(user);
        this.acctime=LocalDateTime.now();
        this.status=AcceptStatus.REGISTER;
        this.connectRequest(request);
    }

    //---연관관계 메서드---
    public void connectUser(User user){
        this.userA=user;
        user.getAccepts().add(this);//유저에 변경사항 적용
    }

    public void connectRequest(Request request){//이 메서드로 Accept와 Request의 연관관계를 매핑하며, Request는 Accept에게 종속된다.
        request.setAccept(this);
        request.setStatus(RequestStatus.ACCEPT);
        this.request=request;
    }

    public void changeStatus(AcceptStatus status){
        if(status.equals(AcceptStatus.COMPLETE)){
            this.status=AcceptStatus.COMPLETE;

            this.request.setStatus(RequestStatus.COMPLETE);
        }

        if(status.equals(AcceptStatus.CANCEL)){
            this.status=AcceptStatus.CANCEL;

            if(this.getRequest().getExectime().isBefore(LocalDateTime.now())){
                this.request.setStatus(RequestStatus.CANCEL);
            } else{
                this.request.setStatus(RequestStatus.REGISTER);
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