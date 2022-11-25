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

    public void sendMail(String email){
        ArrayList<String> toUserList=new ArrayList<>();
        toUserList.add(email);

        int toUserSize=toUserList.size();

        SimpleMailMessage simpleMessage=new SimpleMailMessage();

        simpleMessage.setTo((String[]) toUserList.toArray(new String[toUserSize]));

        simpleMessage.setSubject("[세모봉] 봉사활동이 바로 다음날이에요!");

        simpleMessage.setText("늦지않게 준비해주세요:)");

        javaMailSender.send(simpleMessage);
    }
}
