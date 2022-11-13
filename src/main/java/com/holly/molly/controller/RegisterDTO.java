package com.holly.molly.controller;

import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

public class RegisterDTO {
    @Getter @Setter
    private String name;//form.html post시 name속성으로 찾아 대입해줌. private.

    @Getter @Setter
    private String email;

    @Getter @Setter
    private String password;

    @Getter @Setter
    private String phone;

    @Getter @Setter
    private String birth;

    @Getter @Setter
    private String pid;
}
