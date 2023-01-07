package com.holly.molly.domain;

import com.holly.molly.DTO.RequestDTO;
import com.holly.molly.DTO.ReviewDTO;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends JpaBaseEntity{
    @Id
    @GeneratedValue
    @Column(name="review_id")
    Long id;

    @Column(nullable = false)
    String title;

    @Column(nullable = false)
    String content;

    @ElementCollection(fetch = FetchType.LAZY)//1:N매핑
    List<String> comment;

    Boolean isRequest;

    public Review(String title, String content, Boolean isRequest){
        this.title=title;
        this.content=content;
        this.isRequest=isRequest;
    }

    public Review(ReviewDTO reviewDTO){
        this.title=reviewDTO.getTitle();
        this.content=reviewDTO.getContent();
        this.isRequest=reviewDTO.getIsRequest();
    }

    //MultipartFile image;
}
