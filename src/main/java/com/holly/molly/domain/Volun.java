package com.holly.molly.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Volun {
    @Id
    @GeneratedValue
    @Column(name="voluns")
    private Long id;

    @OneToOne(mappedBy="volunR", fetch=FetchType.LAZY)
    private Request request;

    @OneToOne(mappedBy="volunA", fetch=FetchType.LAZY)
    private Accept accept;

    private LocalDateTime exectime;

    @Enumerated(EnumType.STRING)
    private VolunStatus status;

    private String address;

    //---연관관계 메서드---
    //생성 메서드
    public static Volun createVolun(Request request, LocalDateTime time){
        Volun volun=new Volun();
        volun.setRequest(request);
        volun.setAccept(new Accept());
        volun.setExectime(time);
        volun.setStatus(VolunStatus.ACCEPT);

        return volun;
    }
}
