package com.holly.molly.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LoginDTO {
    private String email;
    private String password;

    private String latitude;
    private String longitude;
}