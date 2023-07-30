package com.clau.gpt.annotation.aspect;

import com.clau.gpt.annotation.processor.ChatGptAnnotationProcessor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@Aspect
@Component
public class ChatGptQuestionAspect {

    @Autowired
    private ChatGptAnnotationProcessor chatGptAnnotationProcessor;

    public ChatGptQuestionAspect() {
        System.out.println("创建切面");
    }

    @Pointcut("@annotation(com.clau.gpt.annotation.ChatGptQuestion)")
    public void withGptChatAnnotation() {}

    @Around("withGptChatAnnotation()")
    public Object processGptChatAnnotation(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("执行切面");
        Object result = joinPoint.proceed(); // 执行目标方法
        chatGptAnnotationProcessor.process(result);
        return result;
    }

    @Pointcut("execution(@com.clau.gpt.annotation.ChatGptQuestion * *(..))")
    public void annotatedMethodWithGptChat() {}

    @AfterReturning("annotatedMethodWithGptChat()")
    public void processGptChatAnnotation(JoinPoint joinPoint) throws IllegalAccessException {
        System.out.println("执行切面");
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            chatGptAnnotationProcessor.process(arg);
        }
    }
}
