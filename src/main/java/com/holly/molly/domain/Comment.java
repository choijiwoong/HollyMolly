package com.holly.molly.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter@Setter
@MappedSuperclass
public abstract class Comment {
    protected LocalDateTime posttime;

    protected String name;

    protected String content;
}
