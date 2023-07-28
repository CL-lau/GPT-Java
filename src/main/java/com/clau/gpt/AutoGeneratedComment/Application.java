package com.clau.gpt.AutoGeneratedComment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    public static void main(String[] args) {
        ExampleClass example = new ExampleClass();
        int result = example.performAction("Hello, GPT!");
        System.out.println("Result: " + result);
    }
}
