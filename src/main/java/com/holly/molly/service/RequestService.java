package com.holly.molly.service;

import com.holly.molly.DTO.LocationDTO;
import com.holly.molly.DTO.RequestDTO;
import com.holly.molly.domain.Request;
import com.holly.molly.domain.RequestStatus;
import com.holly.molly.domain.User;
import com.holly.molly.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;
    private final UserService userService;
    //********************DB************************
    @Transactional
    public Long join(Request request){
        validateDuplicateRequest(request);//request의 user가 올린 requests에서 중복되는 exectime & address 확인
        checkExectime(request);//과거의 시간을 등록하려는지 확인
        requestRepository.save(request);
        return request.getId();
    }

    @Transactional
    public Long delete(Request request){
        if(Optional.ofNullable(requestRepository.findOne(request.getId())).isEmpty()){
            throw new RuntimeException("삭제하려는 Request가 존재하지 않습니다.");
        }
        requestRepository.delete(request);
        return request.getId();
    }

    public Optional<Request> findOne(Long id){
        return Optional.ofNullable(requestRepository.findOne(id));
    }

    public List<Request> findByUser(User user){
        return requestRepository.findByUser(user);
    }

    public List<Request> findByStatus(RequestStatus requestStatus){
        return requestRepository.findByStatus(requestStatus);
    }
    public List<Request> findAll(){
        return requestRepository.findAll();
    }

    //----내부로직----
    private void validateDuplicateRequest(Request request) {
        if(!this.findByUser(request.getUserR()).stream().filter(
                        r->r.getExectime().equals(request.getExectime())
                                &r.getAddress().equals(request.getAddress()))
                .toList().isEmpty()){
            throw new IllegalStateException("이미 존재하는 봉사요청입니다.");
        }
    }

    private void checkExectime(Request request) {
        if(request.getExectime().isBefore(LocalDateTime.now())){
            throw new IllegalStateException("수행시간이 현재시간보다 이전입니다.");
        }
    }

    //********************서비스로직************************
    @Transactional
    public void SrvCreateRequest(Cookie cookie, RequestDTO requestDTO) {
        User userInfo = userService.parseUserCookie(cookie);
        Request request = new Request(userInfo, requestDTO.getExectime(), requestDTO.getAddress(), requestDTO.getContent());
        this.join(request);
    }

    public HashMap<Long, String> findKakaomapList(){
        List<Request> requests = requestRepository.findByStatus(RequestStatus.REGISTER);
        Map<Long, String> kakaomapList=requests.stream().collect(Collectors.toMap(Request::getId, Request::getAddress));
        return new HashMap<Long, String>(kakaomapList);
    }

    /*public HashMap<Long, String> nearVolun(LocationDTO locationDTO){
        List<Request> requests=requestRepository.findByStatus(RequestStatus.REGISTER);
        ArrayList<Double> distances=new ArrayList<Double>();
        for(Request request: requests){
            distances.add(Math.sqrt(Math.pow(Long.parseLong(request.getLatitude())-Long.parseLong(locationDTO.getLatitude()), 2)+
                    Math.pow(Long.parseLong(request.getLongitude())-Long.parseLong(locationDTO.getLongitude()), 2));
        }
    }*/
}
