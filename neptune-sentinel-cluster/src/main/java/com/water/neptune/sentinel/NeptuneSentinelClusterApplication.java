package com.water.neptune.sentinel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class NeptuneSentinelClusterApplication {

    public static void main(String[] args) {
        SpringApplication.run(NeptuneSentinelClusterApplication.class, args);
    }

}
