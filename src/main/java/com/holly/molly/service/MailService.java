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

    public void advanceNotice(String email){
        ArrayList<String> toUserList=new ArrayList<>();
        toUserList.add(email);

        this.sendMail(toUserList, "[세모봉] 봉사활동이 바로 다음날이에요!", "늦지않게 준비해주세요:)");
    }

    public void requestReview(String email, Long id, Boolean isUserR){
        ArrayList<String> toUserList=new ArrayList<>();
        toUserList.add(email);

        String reviewUrl="http://localhost:8080/review/";
        if(isUserR){
            reviewUrl+="r/"+id;
        } else{
            reviewUrl+="a/"+id;
        }

        this.sendMail(toUserList, "[세모봉] 오늘의 봉사활동은 어떠셨나요?", "아래의 링크를 통해 후기를 남겨주세요!\n"+reviewUrl);
    }

    private void sendMail(ArrayList<String> emailTargets, String title, String content){
        int toUserSize=emailTargets.size();

        SimpleMailMessage simpleMessage=new SimpleMailMessage();

        simpleMessage.setTo((String[]) emailTargets.toArray(new String[toUserSize]));

        simpleMessage.setSubject(title);

        simpleMessage.setText(content);

        javaMailSender.send(simpleMessage);
    }
}
