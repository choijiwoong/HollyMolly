package com.holly.molly.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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

    //MultipartFile image;
}
