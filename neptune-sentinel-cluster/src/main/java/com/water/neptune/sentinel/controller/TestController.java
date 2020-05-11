package com.water.neptune.sentinel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author : zhangmiaojie
 * @date : 2020/5/9 15:38
 */
@RequestMapping(value = "/test")
@RestController
public class TestController {
    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping(value = "/user/{userId}")
    public String getUserById(@PathVariable Long userId) {
        ServiceInstance serviceInstance = loadBalancerClient.choose("neptune-user-center");
        String url = String.format("http://%s:%s/hello", serviceInstance.getHost(), serviceInstance.getPort());
        System.out.println("requestUrl -> " + url);
        String result = restTemplate.getForObject(url, String.class);
        System.out.println(result);
        return result;
    }
}
