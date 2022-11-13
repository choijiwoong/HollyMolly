package com.holly.molly.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class User {//피봉사자
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

    @OneToMany(mappedBy = "user")//자기 맴버변수 이름을 참조
    private List<Request> requests=new ArrayList<>();

    @OneToMany(mappedBy = "user")//자기 맴버변수 이름을 참조
    private List<Accept> accepts=new ArrayList<>();
}
