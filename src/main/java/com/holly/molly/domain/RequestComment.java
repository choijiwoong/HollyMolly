package com.holly.molly.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class RequestComment extends Comment{
    @Id
    @GeneratedValue
    @Column(name="comment_id")
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="request_id")
    private Request request;

    //<----연관관계 매핑----->
    public void setRequest(Request request){
        this.request=request;
        request.getComments().add(this);
    }
}
