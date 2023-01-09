package com.holly.molly.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class RequestDTO {
    private String exectime;
    private String address;
    private String content;
}
