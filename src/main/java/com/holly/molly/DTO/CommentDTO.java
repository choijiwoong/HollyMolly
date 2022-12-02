package com.holly.molly.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class CommentDTO {
    private String content;

    private String hid;

    public Long getHid(){
        return Long.parseLong(this.hid);
    }
}
