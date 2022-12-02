package com.holly.molly.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public abstract class Comment {
    private LocalDateTime posttime;

    private String name;

    private String content;
}
