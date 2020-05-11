package com.water.neptune.usercenter.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/hello")
    public String hello() {
        return "hello sentinel";
    }

    @GetMapping("/degrade")
    public String degrade() {
        int i = 0 /0 ;
        return "degrade sentinel";
    }
}