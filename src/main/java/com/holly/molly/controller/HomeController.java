package com.holly.molly.controller;

import com.holly.molly.DTO.LocationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequiredArgsConstructor
public class HomeController {
    @GetMapping("/intro")
    public String home(){
        return "homeTemplate/Nhome";
    }

    @ResponseBody
    @RequestMapping(value="/sendLocLat", method= RequestMethod.POST)
    public void sortedNearReq(LocationDTO locationDTO, HttpServletResponse response) throws Exception{//@PathVariable("latitude") String latitude, @PathVariable("longitude") String longitude) throws Exception{
        System.out.println("sahjkahkdhaskjf");
        checkIsLocation(locationDTO);

        Cookie idCookie1=new Cookie("latitude", locationDTO.getLatitude());
        idCookie1.setPath("/");

        Cookie idCookie2=new Cookie("longitude", locationDTO.getLongitude());
        idCookie2.setPath("/");

        response.addCookie(idCookie1);
        response.addCookie(idCookie2);
    }

    private void checkIsLocation(LocationDTO locationDTO) {//lat 37.5381311 lng 126.9136286
        String longitude= locationDTO.getLongitude();
        String latitude=locationDTO.getLatitude();
        if(latitude.isEmpty() || longitude.isEmpty())
            throw new RuntimeException("locationDTO is empty");

        try {
            longitude.matches("[0-9]+\\.[0-9]");
            latitude.matches("[0-9]+\\.[0-9]");
        } catch(Exception e){
            throw new RuntimeException("locationDTO's info has wrong format");
        }
    }
}
