package com.clau.gpt.prompt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.clau.gpt")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

