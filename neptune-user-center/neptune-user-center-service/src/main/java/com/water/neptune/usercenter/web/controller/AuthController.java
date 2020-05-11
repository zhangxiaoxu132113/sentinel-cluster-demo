package com.water.neptune.usercenter.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @Author: zhangmiaojie
 * @Date: 2020/4/30 15:06
 */
@RequestMapping(value = "auth")
@RestController
public class AuthController {


    @GetMapping("/member")
    public Principal user(Principal member) {
        return member;
    }

}
