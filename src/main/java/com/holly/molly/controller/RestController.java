package com.holly.molly.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

@org.springframework.web.bind.annotation.RestController
public class RestController {
    @RequestMapping(value="ajax", method= RequestMethod.POST)
    public ResponseEntity<String> ajaxTest(){
        System.out.println("********work*******");
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
