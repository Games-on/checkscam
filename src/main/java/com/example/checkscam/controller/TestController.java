package com.example.checkscam.controller;

import com.example.checkscam.util.error.IdInvalidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TestController {
    @GetMapping("/")
    public String getHelloWorld() throws IdInvalidException {
        return "Hello world";
    }
}
