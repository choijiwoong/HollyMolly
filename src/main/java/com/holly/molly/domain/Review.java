package com.holly.molly.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {
    @Id
    @GeneratedValue
    @Column(name="review_id")
    Long id;

    @Column(nullable = false)
    String title;

    @Column(nullable = false)
    String content;

    @ElementCollection//1:N매핑
    List<String> comment;

    public Review(String title, String content){
        this.title=title;
        this.content=content;
    }

    //MultipartFile image;
}
