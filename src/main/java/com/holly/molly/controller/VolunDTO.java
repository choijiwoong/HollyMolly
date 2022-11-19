package com.holly.molly.controller;

import com.holly.molly.domain.VolunStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Getter
@Setter
public class VolunDTO {
    private String exectime;
    private String address;
}
