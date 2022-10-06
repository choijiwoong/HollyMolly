package com.holly.molly.controller;

public class MemberForm {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String name;//form.html post시 name속성으로 찾아 대입해줌. private.
    private String emailAddress;
    private String password;

}
