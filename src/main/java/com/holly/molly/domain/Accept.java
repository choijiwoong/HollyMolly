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
        this.status=AcceptStatus.REGISTER;
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
        //RequestStatus: REGISTER, ACCEPT, CANCEL, MAILED, RUNNING, EMERGENCY, COMPLETE, REVIEWD
        //AcceptStatus: REGISTER, CANCEL, MAILED, RUNNING, EMERGENCY, COMPLETE, REVIEWD

        if(this.getStatus()==status)
            return;

        //if문 순서 주의. 절차적으로 진행됨

        if(status.equals(AcceptStatus.REGISTER)){//register변경 시 제약X
            throw new RuntimeException("[DEBUG] 잘못된 AcceptStatus조작입니다! REGISTER로의 변경은 생성시에만 가능합니다. 현재상태: "+this.getStatus());
        }

        if(status.equals(AcceptStatus.CANCEL)){//cancel변경 시
            if(this.getStatus()==AcceptStatus.REGISTER || this.getStatus()==AcceptStatus.MAILED) {//봉사 진행 전에만 가능
                this.status = AcceptStatus.CANCEL;//accept의 상태를 cancel로 변경
                this.getRequest().changeStatus(RequestStatus.CANCEL);//Request의 상태를 cancel로 변경
            } else{
                throw new RuntimeException("[DEBUG] 잘못된 AcceptStatus조작입니다! REGISTER혹은 MAILED상태에서만 CANCEL로 변경할 수 있습니다. 현재상태: "+this.getStatus());
            }
        }

        if(status.equals(AcceptStatus.MAILED)){//MAILED변경 시
            if(this.getStatus()==AcceptStatus.REGISTER) {
                this.status = AcceptStatus.MAILED;
                this.getRequest().changeStatus(RequestStatus.MAILED);
            } else {
                throw new RuntimeException("[DEBUG] 잘못된 AcceptStatus조작입니다! REGISTER생태에서만 MAILED로 변경할 수 있습니다. 현재상태: "+this.getStatus());
            }
        }

        if(status.equals(AcceptStatus.RUNNING)){//RUNNING변경 시
            if(this.getStatus()==AcceptStatus.MAILED) {
                this.status = AcceptStatus.RUNNING;
                this.getRequest().changeStatus(RequestStatus.RUNNING);
            } else {
                throw new RuntimeException("[DEBUG] 잘못된 AcceptStatus조작입니다! MAILED상태에서만 RUNNING으로 변경할 수 있습니다. 현재상태: "+this.getStatus());
            }
        }

        if(status.equals(AcceptStatus.EMERGENCY)){
            if(this.getStatus()==AcceptStatus.RUNNING) {
                this.status = AcceptStatus.EMERGENCY;
                this.getRequest().changeStatus(RequestStatus.EMERGENCY);
            } else {
                throw new RuntimeException("[DEBUG] 잘못된 AcceptStatus조작입니다! RUNNING상태에서만 EMERGENCY로 변경할 수 있습니다. 현재상태: "+this.getStatus());
            }
        }

        if(status.equals(AcceptStatus.COMPLETE)) {
            if(this.getStatus()==AcceptStatus.RUNNING){
                this.status = AcceptStatus.COMPLETE;
                this.request.changeStatus(RequestStatus.COMPLETE);
            } else{
                throw new RuntimeException("[DEBUG] 잘못된 AcceptStatus조작입니다! RUNNING상태에서만 COMPLETE로 변경할 수 있습니다. 현재상태: "+this.getStatus());
            }
        }

        if(status.equals(AcceptStatus.REVIEWD)){
            if(this.getStatus()==AcceptStatus.COMPLETE){
                this.status = AcceptStatus.REVIEWD;
                //this.request.changeStatus(RequestStatus.REVIEWD);//리뷰는 종속성X
            } else{
                throw new RuntimeException("[DEBUG] 잘못된 AcceptStatus조작입니다! COMPLETE상태에서만 REVIEWED로 변경할 수 있습니다. 현재상태: "+this.getStatus());
            }
        }
    }
}