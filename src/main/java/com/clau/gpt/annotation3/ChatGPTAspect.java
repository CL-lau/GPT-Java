package com.clau.gpt.annotation3;

import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ChatGPTAspect {

    @Pointcut("set(@com.example.ChatGPTAnnotation * *) && @annotation(annotation)")
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
