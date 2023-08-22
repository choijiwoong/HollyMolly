package com.holly.molly.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AdvanceMailDTO {
    private String requsrEmailAddress;
    private String acqusrEmailAddress;
    //private String startLink;
    //private String endLink;
    private String emergencyLink;
}
