package com.holly.molly.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter@Setter
@MappedSuperclass
public abstract class Comment {
    @Column(nullable = false)
    protected LocalDateTime posttime;

    @Column(nullable = false)
    protected String name;

    @Column(nullable = false)
    protected String content;
}
