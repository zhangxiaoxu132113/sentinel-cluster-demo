package com.water.neptune.usercenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author zhangmiaojie
 */
@EnableDiscoveryClient
@SpringBootApplication
public class NeptuneUserCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(NeptuneUserCenterApplication.class, args);
    }

}
