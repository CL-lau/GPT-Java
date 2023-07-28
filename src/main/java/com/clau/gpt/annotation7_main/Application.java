package com.clau.gpt.annotation7_main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class Application {

    public static void main(String[] args) {
        MyClass myClass = new MyClass();

        // 触发自动转换
        // MyClass 中带有 ChatGPTAnnotation 注解的字段会自动转换为答案
        // MyClass 中带有 ChatGPTListAnnotation 注解的 List 字段中的字符串元素会自动转换为答案
    }
}

