package com.clau.gpt.annotation.iInterceptor;

import com.clau.gpt.annotation.ChatGptQuestion;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import java.lang.reflect.Method;

@Component
public class IdempotentInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("执行了拦截器……");
        if (!(handler instanceof HandlerMethod)){
            return true;
        }
        Method method = ((HandlerMethod) handler).getMethod();
        ChatGptQuestion chatGptQuestion = method.getAnnotation(ChatGptQuestion.class);

        if (chatGptQuestion != null){

        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}

