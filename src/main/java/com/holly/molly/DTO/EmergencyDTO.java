package com.holly.molly.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EmergencyDTO {
    private Long requestId;
    private String execTime;
    private String address;
    private String phoneUserR;
    private String phoneUserA;
}
