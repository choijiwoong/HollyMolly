package com.holly.molly.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@Getter@Setter@AllArgsConstructor
public class CommentDTO {
    private String content;

    private String hid;

    public Long getHid(){
        return Long.parseLong(this.hid);
    }
}
