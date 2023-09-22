package com.holly.molly.controller;

import com.holly.molly.DTO.ReviewDTO;
import com.holly.molly.VO.TestVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@org.springframework.web.bind.annotation.RestController
public class RestController {//ajax를 이용한 Rest API방식 컨트롤러. 개발중.
    @RequestMapping(value="ajax", method= RequestMethod.POST)
    public ResponseEntity<String> ajaxTest(){
        System.out.println("********work*******");
        return new ResponseEntity<>(HttpStatus.OK);
    }//postman으로 http://localhost:8080/ajax시 정상적으로 1이 반환됨.

    @RequestMapping (value="/rest/test1", method=RequestMethod.POST)
    public ResponseEntity<String> test1(@RequestBody TestVO testVO) throws Exception{
        System.out.println("[RESULT]"+ testVO.getName());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value="/rest/test2", method=RequestMethod.GET)
    public TestVO test2(){
        TestVO testVO=new TestVO();
        testVO.setName("hello");
        return testVO;
    }
}
