package com.example.checkscam.controller;

import com.example.checkscam.service.error.IdInvalidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TestController {
    @GetMapping("/")
    public String getHelloWorld() throws IdInvalidException {
        if (true)
            throw new IdInvalidException("check mate hoidanit");
        return "hello world";
    }
}
