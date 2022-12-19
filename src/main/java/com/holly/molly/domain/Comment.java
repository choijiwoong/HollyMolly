package com.holly.molly.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public abstract class Comment extends JpaBaseEntity{
    @Column(nullable = false)
    protected String name;

    @Column(nullable = false)
    protected String content;
}
