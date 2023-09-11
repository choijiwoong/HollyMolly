package com.holly.molly.domain;

import com.holly.molly.DTO.RequestDTO;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.bytebuddy.asm.Advice;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Request extends JpaBaseEntity{
    @Id
    @GeneratedValue//중복불가_DB에 위임
    @Column(name="request_id")
    private Long id;//내부적으로 사용할 unique key값

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="userR_id")
    private User userR;//봉사 요청자 정보

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RequestStatus status;//봉사 진행 상태 정보. 절차적으로 진행

    @Column(nullable = false)@Setter//[DEBUG] test for making situation(시간을 조작하여 정상적으로 STATUS가 바뀌는지 생성자 외 조작 임시가능)
    private LocalDateTime exectime;

    @Column(nullable = false)
    private String address;//봉사 진행 장소

    @Column(nullable = false)
    private String duration;//봉사시간

    @Column(nullable = false)
    private String content;//봉사 내용

    @Column(nullable = false)
    private String longitude;//위치정보
    @Column(nullable = false)
    private String latitude;

    @OneToOne(fetch =FetchType.LAZY)
    private Accept accept;

    @OneToMany(mappedBy = "request")
    private List<RequestComment> comments=new ArrayList<>();

    public Request(User user, LocalDateTime exectime, String address, String content, String latitude, String longitude, String duration){
        this.connectUser(user);
        this.status=RequestStatus.REGISTER;
        this.exectime=exectime;
        this.address=address;
        this.content=content;
        this.longitude=latitude;
        this.latitude=longitude;
        this.duration=duration;
    }

    public Request(User user, String exectime, String address, String content, String latitude, String longitude, String duration){
        this.connectUser(user);
        this.status=RequestStatus.REGISTER;
        this.exectime=this.parseStringDate(exectime);
        this.address=address;
        this.content=content;
        this.longitude=latitude;
        this.latitude=longitude;
        this.duration=duration;
    }

    public Request(User user, RequestDTO requestDTO){
        this.connectUser(user);
        this.status=RequestStatus.REGISTER;
        this.exectime=this.parseStringDate(requestDTO.getExectime());
        this.address=requestDTO.getAddress();
        this.content=requestDTO.getContent();
        this.longitude=requestDTO.getLongitude();
        this.latitude=requestDTO.getLatitude();
        this.duration=requestDTO.getDuration();
    }

    //---연관관계 메서드---
    public void connectUser(User user){//유저와 request를 연결
        this.userR=user;
        user.getRequests().add(this);//[DEBUG]이런식으로 직접 연관관계를 조작하는게 맞나..? RequestRequestCommentServiceText.assosiationTest에서 연관관계 오류 발생
    }

    public void setAccept(Accept accept){//request와 accept 연결
        if(this.getStatus()==RequestStatus.REGISTER)
            this.accept=accept;
        else
            throw new RuntimeException("[DEBUG] REGISTER 상태가 아닌 request에 accept를 시도하였습니다!");
    }

    public void changeStatus(RequestStatus status){//request 상태변경. 이는 등록이후 전적으로 accept.changeStatus()메서드에 의해 통합하여 조작.
        //RequestStatus: REGISTER, ACCEPT, CANCEL, MAILED, RUNNING, EMERGENCY, COMPLETE, REVIEWD
        if(this.getStatus()==status)
            return;

        //예외조건들
        if(status.equals(RequestStatus.REGISTER)){
            throw new RuntimeException("[DEBUG] 잘못된 RequestStatus조작입니다! REGISTER로의 변경은 생성시에만 가능합니다.  현재상태: "+this.getStatus());
        }

        if(status.equals(RequestStatus.ACCEPT)){//accept변경 시
            if(this.getStatus()!=RequestStatus.REGISTER)
                throw new RuntimeException("[DEBUG] 잘못된 RequestStatus조작입니다! REGISTER상태에서만 ACCEPT로 변경할 수 있습니다. 현재상태: "+this.getStatus());
        }

        if(status.equals(RequestStatus.CANCEL)){//cancel변경 시
            if(this.getStatus().equals(RequestStatus.REGISTER) || this.getStatus().equals(RequestStatus.MAILED) || this.getStatus().equals(RequestStatus.ACCEPT)) {
            } else {
                throw new RuntimeException("[DEBUG] 잘못된 RequestStatus조작입니다! REGISTER혹은 MAILED혹은 ACCEPT상태에서만 CANCEL로 변경할 수 있습니다. 현재상태: " + this.getStatus());
            }
        }

        if(status.equals(RequestStatus.MAILED)){//mailed변경 시
            if(this.getStatus()!=RequestStatus.ACCEPT)
                throw new RuntimeException("[DEBUG] 잘못된 RequestStatus조작입니다! ACCEPT상태에서만 MAILED로 변경할 수 있습니다. 현재상태: "+this.getStatus());
        }

        if(status.equals(RequestStatus.RUNNING)){//running변경 시
            if(this.getStatus()!=RequestStatus.MAILED)
                throw new RuntimeException("[DEBUG] 잘못된 RequestStatus조작입니다! MAILED상태에서만 RUNNING으로 변경할 수 있습니다. 현재상태: "+this.getStatus());
        }

        if(status.equals(RequestStatus.EMERGENCY)){//emergency변경 시
            if(this.getStatus()!=RequestStatus.RUNNING)
                throw new RuntimeException("[DEBUG] 잘못된 RequestStatus조작입니다! RUNNING상태에서만 EMERGENCY로 변경할 수 있습니다. 현재상태: "+this.getStatus());
        }

        if(status.equals(RequestStatus.COMPLETE)){//complete변경 시
            if(this.getStatus()!=RequestStatus.RUNNING)
                throw new RuntimeException("[DEBUG] 잘못된 RequestStatus조작입니다! RUNNING상태에서만 COMPLETE로 변경할 수 있습니다. 현재상태: "+this.getStatus());
        }

        if(status.equals(RequestStatus.REVIEWD)){//revied변경 시
            if(this.getStatus()!=RequestStatus.COMPLETE)
                throw new RuntimeException("[DEBUG] 잘못된 RequestStatus조작입니다! COMPLETE상태에서만 REVIED로 변경할 수 있습니다. 현재상태: "+this.getStatus());
            this.status=RequestStatus.REVIEWD;//리뷰는 종속성 X
        }

        //예외조건 통과시에 변경
        this.status = status;
    }

    private LocalDateTime parseStringDate(String dateInfo){//0000.00.00.00.00형식의 데이터인지 확인
        String datePattern="^[0-9]{4}.[0-9]{2}.[0-9]{2}.[0-9]{2}.[0-9]{2}$";
        if(!Pattern.matches(datePattern, dateInfo))
            throw new RuntimeException("봉사요청시 입력한 날짜 형식이 올바르지 않습니다!");

        Integer year = Integer.parseInt(dateInfo.substring(0, 4));
        Integer month = Integer.parseInt(dateInfo.substring(5, 7));
        Integer date = Integer.parseInt(dateInfo.substring(8, 10));
        Integer hour = Integer.parseInt(dateInfo.substring(11, 13));
        Integer minute = Integer.parseInt(dateInfo.substring(14, 16));

        return LocalDateTime.of(year, month, date, hour, minute);
    }
}
