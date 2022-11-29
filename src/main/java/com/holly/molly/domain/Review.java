package com.holly.molly.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Review {
    @Id
    @GeneratedValue
    @Column(name="review_id")
    Long id;

    String title;

    String content;

    @ElementCollection//1:N매핑
    List<String> comment;

    //MultipartFile image;
}
