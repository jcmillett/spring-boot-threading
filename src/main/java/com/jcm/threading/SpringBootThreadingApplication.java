package com.jcm.threading;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringBootThreadingApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootThreadingApplication.class, args);
    }
}
