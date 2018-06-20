package com.alessandrodias.miniredis.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.alessandrodias.miniredis")
@SpringBootApplication
public class MiniRedisWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiniRedisWebApplication.class, args);
    }
}
