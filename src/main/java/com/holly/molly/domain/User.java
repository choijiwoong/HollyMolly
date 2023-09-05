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
public class User extends JpaBaseEntity{//추후 사진정보도 받을건데, DB에 저장이 아닌 서버컴에 저장하여 id별로 찾아 쓸 예정
    @Id
    @GeneratedValue
    @Column(name="user_id")//고유(중복불가)
    private Long id;//내부적으로 사용할 unique key

    @Column(nullable = false)
    private String name;//이름

    @Column(unique = true, nullable = false)//고유(중복불가)
    private String email;//이메일 주소

    @Column(nullable = false)
    private String password;//비밀번호

    @Column(unique = true, nullable = false)
    private String phone;//전화번호

    @Column(unique = true, nullable = false)
    private String pid;//주민등록번호

    @OneToMany(mappedBy = "userR")
    private List<Request> requests=new ArrayList<>();

    @OneToMany(mappedBy="userA")
    private List<Accept> accepts=new ArrayList<>();

    public User(String name, String email, String password, String phone, String pid){//기본 생성자
        this.name=name;
        this.email=email;
        this.password=password;
        this.phone=phone;
        this.pid=pid;
    }

    public User(UserDTO userDTO){//DTO이용 생성자
        this.name= userDTO.getName();
        this.email= userDTO.getEmail();
        this.password= userDTO.getPassword();
        this.phone= userDTO.getPhone();
        this.pid= userDTO.getPid();
    }
}
