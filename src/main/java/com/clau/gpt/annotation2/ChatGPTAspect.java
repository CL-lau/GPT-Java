package com.clau.gpt.annotation2;


//<dependency>
//<groupId>org.aspectj</groupId>
//<artifactId>aspectjrt</artifactId>
//<version>1.9.7</version>
//</dependency>

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import java.lang.reflect.Field;

@Aspect
public class ChatGPTAspect {

    @Pointcut("set(@ChatGPTAnnotation * *) && @annotation(annotation)")
    public void fieldAnnotatedWithChatGPT(ChatGPTAnnotation annotation) {
    }

    @AfterReturning(pointcut = "fieldAnnotatedWithChatGPT(annotation)", returning = "fieldValue")
    public void convertString(Field field, ChatGPTAnnotation annotation, Object fieldValue) {
        if (field.getType() == String.class) {
            String appKey = annotation.appKey();
            String originalString = (String) fieldValue;
            String answer = getAnswerFromChatGPT(originalString, appKey);
            try {
                field.set(fieldValue, answer);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private String getAnswerFromChatGPT(String question, String appKey) {
        // 实现访问 ChatGPT API 并获取答案的逻辑
        // ...
        // ...
    }
}

//<!DOCTYPE aspectj PUBLIC
//        "-//AspectJ//DTD//EN" "http://www.eclipse.org/aspectj/dtd/aspectj.dtd">
//
//<aspectj>
//<aspects>
//<aspect name="com.example.ChatGPTAspect" />
//</aspects>
//</aspectj>
