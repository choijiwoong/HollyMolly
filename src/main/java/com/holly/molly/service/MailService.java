package com.holly.molly.service;

import com.holly.molly.DTO.AdvanceMailDTO;
import com.holly.molly.DTO.EmergencyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.ArrayList;

@Service
public class MailService {
    @Autowired
    private JavaMailSender javaMailSender;
    private String adminAddress="trianglestick0628@gmail.com";

    public void emergencyMail(EmergencyDTO emergencyDTO){//응급메일전송용
        ArrayList<String> emailBuffer=new ArrayList<String>();
        emailBuffer.add(adminAddress);
        this.sendMail(emailBuffer, "[긴급상황] requestId: "+emergencyDTO.getRequestId(),
                "[긴급상황 접수]"+'\n'+'\n'+emergencyDTO.getExecTime()+", "+emergencyDTO.getAddress()+"봉사활동에서 긴급신고가 접수되었습니다"+'\n'+'\n'+
                "피봉사자 전화번호: "+emergencyDTO.getPhoneUserR()+'\n'+"봉사자 전화번호: "+emergencyDTO.getPhoneUserA()+'\n'
                );
    }

    public void advanceNotice(ArrayList<AdvanceMailDTO> DTOs){//봉사알림메일전송용
        for(AdvanceMailDTO DTO: DTOs) {
            ArrayList<String> list=new ArrayList<String>();

            list.add(DTO.getRequsrEmailAddress());//개별로 다른 정보이기에 따로 List형태만 맞추어 1개씩 전송
            this.sendMail(list, "[세모봉] 봉사활동이 바로 다음날이에요!",
                    "늦지않게 준비해주세요:)"+'\n'+"응급상황일 경우에 다음의 링크를 눌러주세요! "+DTO.getEmergencyLink()+'\n'
            );
            list.clear();//리스트 비우기(재활용을 위함)

            list.add(DTO.getAcqusrEmailAddress());
            this.sendMail(list, "[세모봉] 봉사활동이 바로 다음날이에요!",
                    "늦지않게 준비해주세요:)"+'\n'+"응급상황일 경우에 다음의 링크를 눌러주세요! "+DTO.getEmergencyLink()+'\n'
            );
        }
    }

    public void requestReview(String email, Long id, Boolean isUserR){//리뷰요청메일전송용
        ArrayList<String> toUserList=new ArrayList<>();
        toUserList.add(email);

        String reviewUrl="http://localhost:8080/review/"+ ((isUserR)?"r/":"a/")+id;

        this.sendMail(toUserList, "[세모봉] 오늘의 봉사활동은 어떠셨나요?", "아래의 링크를 통해 후기를 남겨주세요!"+'\n'+reviewUrl);
    }

    private void sendMail(ArrayList<String> emailTargets, String title, String content){//실제메일전송로직
        int toUserSize=emailTargets.size();

        SimpleMailMessage simpleMessage=new SimpleMailMessage();
        simpleMessage.setTo((String[]) emailTargets.toArray(new String[toUserSize]));
        simpleMessage.setSubject(title);
        simpleMessage.setText(content);

        javaMailSender.send(simpleMessage);
    }
}
