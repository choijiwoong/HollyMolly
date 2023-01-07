package com.holly.molly.domain;

import com.holly.molly.DTO.RequestDTO;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Request extends JpaBaseEntity{
    @Id
    @GeneratedValue
    @Column(name="request_id")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="userR_id")
    private User userR;

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

    public Request(User user, String exectime, String address, String content){
        this.connectUser(user);
        this.status=RequestStatus.REGISTER;
        this.exectime=this.parseStringDate(exectime);
        this.address=address;
        this.content=content;
    }

    public Request(User user, LocalDateTime exectime, String address, String content){
        this.connectUser(user);
        this.status=RequestStatus.REGISTER;
        this.exectime=exectime;
        this.address=address;
        this.content=content;
    }

    public Request(User user, RequestDTO requestDTO){
        this.connectUser(user);
        this.status=RequestStatus.REGISTER;
        this.exectime=this.parseStringDate(requestDTO.getExectime());
        this.address=requestDTO.getAddress();
        this.content=requestDTO.getContent();
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

    private LocalDateTime parseStringDate(String dateInfo){//0000.00.00.00.00
        String datePattern="^[0-9]{4}.[0-9]{2}.[0-9]{2}.[0-9]{2}.[0-9]{2}$";
        if(!Pattern.matches(datePattern, dateInfo))
            throw new RuntimeException("봉사요청시 입력한 날짜 형식이 올바르지 않습니다!");

        Integer year = Integer.parseInt(dateInfo.substring(0, 4));
        Integer month = Integer.parseInt(dateInfo.substring(5, 7));
        Integer date = Integer.parseInt(dateInfo.substring(8, 10));
        Integer hour = Integer.parseInt(dateInfo.substring(11, 13));
        Integer minute = Integer.parseInt(dateInfo.substring(14, 16));

        return LocalDateTime.of(year, month, date, hour, minute);
    }
}
