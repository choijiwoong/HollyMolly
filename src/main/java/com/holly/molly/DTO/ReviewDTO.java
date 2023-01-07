package com.holly.molly.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter @AllArgsConstructor
public class ReviewDTO {
    String title;
    String content;
    Boolean isRequest;
    //MultipartFile image;
}
