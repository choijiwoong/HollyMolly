package com.holly.molly.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class UserDTO {
    private String name;//form.html post시 name속성으로 찾아 대입해줌. private.
    private String email;
    private String password;
    private String phone;
    private String pid;
}
