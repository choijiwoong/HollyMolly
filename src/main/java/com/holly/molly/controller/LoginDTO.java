package com.holly.molly.controller;

import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

public class LoginDTO {
    @Getter @Setter
    private String email;

    @Getter @Setter
    private String password;
}