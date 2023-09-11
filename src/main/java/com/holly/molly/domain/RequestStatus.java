package com.holly.molly.domain;

public enum RequestStatus {
    REGISTER,//요청등록
    ACCEPT,//수락완료
    CANCEL,//봉사기간 전 취소
    MAILED,//사전알림 메일여부
    RUNNING,//진행중
    EMERGENCY,//응급상황
    COMPLETE,//봉사완료
    REVIEWD//리뷰작성완료
}
