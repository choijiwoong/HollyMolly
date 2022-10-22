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
    private Long id;

    private String name;

    private String email;

    private String password;

    private String phone;

    @Embedded
    private Date birth;

    private String pid;

    @OneToMany(mappedBy = "user")//자기 맴버변수 이름을 참조
    private List<Request> requests=new ArrayList<>();
}
