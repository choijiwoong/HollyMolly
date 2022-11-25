package com.holly.molly.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.ArrayList;

@Service
public class MailService {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendMail(){
        ArrayList<String> toUserList=new ArrayList<>();
        toUserList.add("e");

        int toUserSize=toUserList.size();

        SimpleMailMessage simpleMessage=new SimpleMailMessage();

        simpleMessage.setTo((String[]) toUserList.toArray(new String[toUserSize]));

        simpleMessage.setSubject("title");

        simpleMessage.setText("Text sample");

        javaMailSender.send(simpleMessage);
    }
}
