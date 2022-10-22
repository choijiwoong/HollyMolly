package com.holly.molly.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Volun {
    @Id
    @GeneratedValue
    @Column(name="voluns")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
    private Request request;

    @OneToOne(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
    private Accept accept;

    private LocalDateTime exectime;

    @Enumerated(EnumType.STRING)
    private VolunStatus status;
}
