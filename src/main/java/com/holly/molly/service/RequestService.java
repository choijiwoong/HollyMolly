package com.holly.molly.service;

import com.holly.molly.DTO.LocationDTO;
import com.holly.molly.DTO.NearRequestListElementDTO;
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
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;
    private final UserService userService;

    private final Double DISTANCE_FIX=15.9493;
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
        Request request = new Request(userInfo, requestDTO.getExectime(), requestDTO.getAddress(), requestDTO.getContent(), requestDTO.getLatitude(), requestDTO.getLongitude());
        this.join(request);
    }

    public HashMap<Long, String> findKakaomapList(){
        List<Request> requests = requestRepository.findByStatus(RequestStatus.REGISTER);
        Map<Long, String> kakaomapList=requests.stream().collect(Collectors.toMap(Request::getId, Request::getAddress));
        return new HashMap<Long, String>(kakaomapList);
    }

    public List<NearRequestListElementDTO> nearVolun(LocationDTO locationDTO, Integer pageSize){//위치정보, 페이지정부
        List<Request> requests=requestRepository.findByStatus(RequestStatus.REGISTER);
        if(!checkIsLocation(locationDTO)) {//LocationDTO유효성 체크.
            return new ArrayList<NearRequestListElementDTO>();
        }

        //해당 위치(locationDTO)로 부터 모든 request 위치의 거리 저장(페이지 결과 거리). ****requests와 distaces의 인덱스는 동일한 request의 정보를 의미한다****
        ArrayList<Double> distances=getDistance(requests, locationDTO);

        List<Request> sortResults=requests.stream().sorted(new Comparator<Request>() {//거리가 적은 순서대로 정렬
            @Override
            public int compare(Request o1, Request o2) {
                Double dis1=Math.sqrt(Math.pow(Double.parseDouble(o1.getLatitude())-Double.parseDouble(locationDTO.getLatitude()), 2)+
                        Math.pow(Double.parseDouble(o1.getLongitude())-Double.parseDouble(locationDTO.getLongitude()), 2));
                Double dis2=Math.sqrt(Math.pow(Double.parseDouble(o2.getLatitude())-Double.parseDouble(locationDTO.getLatitude()), 2)+
                        Math.pow(Double.parseDouble(o2.getLongitude())-Double.parseDouble(locationDTO.getLongitude()), 2));
                if(dis1<dis2)
                    return -1;
                if(dis1>dis2)
                    return 1;
                return 0;
            }
        }).toList();

        ArrayList<NearRequestListElementDTO> results=new ArrayList<NearRequestListElementDTO>();//반환 결과를 저장할 컨테이너.(거리정보가 포함된 새로운 requestDTO를 반환)
        for(Request request: sortResults.subList(0, Math.min(pageSize, sortResults.size()))){
            results.add(new NearRequestListElementDTO(
                    request.getId(), distances.get(requests.indexOf(request)), request.getAddress()
            ));
        }
        return results.stream().toList();
    }

    public ArrayList<Double> getDistance(List<Request> requests, LocationDTO locationDTO) {
        ArrayList<Double> distances=new ArrayList<Double>();

        for(Request request: requests){
            distances.add(Math.sqrt(Math.pow(Double.parseDouble(request.getLatitude())-Double.parseDouble(locationDTO.getLatitude()), 2)+
                    Math.pow(Double.parseDouble(request.getLongitude())-Double.parseDouble(locationDTO.getLongitude()), 2))/DISTANCE_FIX);
        }
        return distances;
    }

    public Boolean checkIsLocation(LocationDTO locationDTO) {//lat 37.5381311 lng 126.9136286
        String longitude= locationDTO.getLongitude();
        String latitude=locationDTO.getLatitude();
        if(longitude.isEmpty() || latitude.isEmpty())
            return false;

        try {
            Double.parseDouble(longitude);
            Double.parseDouble(latitude);
        } catch(NumberFormatException e){
            return false;
        }

        return true;
    }
}
