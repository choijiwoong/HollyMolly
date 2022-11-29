package com.holly.molly.DTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter@Setter
public class ReviewDTO {
    String title;
    String content;
    //MultipartFile image;
}
