package com.holly.molly.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestComment extends Comment{
    @Id
    @GeneratedValue
    @Column(name="comment_id")
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="request_id")
    private Request request;

    public RequestComment(Request request, String name, String content){
        this.connectRequest(request);
        this.name=name;
        this.content=content;
        this.posttime=LocalDateTime.now();
    }

    //<----연관관계 매핑----->
    public void connectRequest(Request request){
        this.request=request;
        request.getComments().add(this);
    }
}
