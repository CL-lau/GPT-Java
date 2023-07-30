package com.clau.gpt.annotation.processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class ChatGptQuestionBeanPostProcessor implements BeanPostProcessor {

    @Autowired
    ChatGptAnnotationProcessor chatGptAnnotationProcessor;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        try {
            chatGptAnnotationProcessor.process(bean);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return bean;
    }
}
