package com.holly.molly.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity
@Getter @Setter
@Table(name="users")//DB SQL user키워드와의 충돌을 방지
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue
    @Column(name="user_id")
    private Long id;//고유(중복불가)

    private String name;

    private String email;//고유(중복불가)

    private String password;

    private String phone;//고유(중복불가)

    private String birth;

    private String pid;//고유(중복불가)

    @OneToMany(mappedBy = "userR")//자기 맴버변수 이름을 참조
    private List<Request> requests=new ArrayList<>();

    @OneToMany(mappedBy="userA")//자기 맴버변수 이름을 참조
    private List<Accept> accepts=new ArrayList<>();

    public User(String name, String email, String password, String phone, String pid){
        this.name=name;
        this.email=email;
        this.password=password;
        this.phone=phone;
        this.birth=pid.substring(0,6);
        this.pid=pid;
    }
}
