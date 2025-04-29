package com.example.checkscam.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/")
    public String getHelloWrold(){
        return "Hello World";
        //test branch
    }
}
