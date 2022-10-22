package com.holly.molly.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter @Setter
public class User1 {
    @Id
    @GeneratedValue
    @Column(name="user1_id")
    private Long id;//고유(중복불가)

    private String name;

    @Id
    private String email;//고유(중복불가)

    private String password;

    @Id
    private String phone;//고유(중복불가)

    @Embedded
    private Date birth;

    @Id
    private String pid;//고유(중복불가)

    @OneToMany(mappedBy = "user")//자기 맴버변수 이름을 참조
    private List<Request> requests=new ArrayList<>();
}
