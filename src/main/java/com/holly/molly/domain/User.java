package com.holly.molly.domain;

import com.holly.molly.DTO.UserDTO;
import lombok.*;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity
@Getter
@Table(name="users")//DB SQL user키워드와의 충돌을 방지
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends JpaBaseEntity{
    @Id
    @GeneratedValue
    @Column(name="user_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;//고유(중복불가)

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String phone;

    @Column(unique = true, nullable = false)
    private String pid;

    @OneToMany(mappedBy = "userR")
    private List<Request> requests=new ArrayList<>();

    @OneToMany(mappedBy="userA")
    private List<Accept> accepts=new ArrayList<>();

    public User(String name, String email, String password, String phone, String pid){
        this.name=name;
        this.email=email;
        this.password=password;
        this.phone=phone;
        this.pid=pid;
    }

    public User(UserDTO userDTO){
        this.name= userDTO.getName();
        this.email= userDTO.getEmail();
        this.password= userDTO.getPassword();
        this.phone= userDTO.getPhone();
        this.pid= userDTO.getPid();
    }
}
