package com.clau.gpt.annotation7_main;

import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import java.lang.reflect.Field;
import java.util.List;

@Aspect
@Component
public class ChatGPTAspect {

    @Pointcut("@annotation(com.example.ChatGPTAnnotation) && execution(* set*(String)) && args(value)")
    public void stringFieldAnnotatedWithValue(String value) {
    }

    @AfterReturning(value = "stringFieldAnnotatedWithValue(value)", argNames = "value")
    public void convertStringField(String value) {
        String appKey = getAppKeyForStringField();
        String answer = getAnswerFromChatGPT(value, appKey);
        setFieldValue(this, getFieldWithValue(value), answer);
    }

    @Pointcut("@annotation(com.example.ChatGPTListAnnotation) && execution(* set*(List<String>)) && args(value)")
    public void listFieldAnnotatedWithValue(List<String> value) {
    }

    @AfterReturning(value = "listFieldAnnotatedWithValue(value)", argNames = "value")
    public void convertListField(List<String> value) {
        String appKey = getAppKeyForListField();
        for (int i = 0; i < value.size(); i++) {
            String originalValue = value.get(i);
            String answer = getAnswerFromChatGPT(originalValue, appKey);
            value.set(i, answer);
        }
    }

    private String getAppKeyForStringField() {
        // 从某处获取 String 字段注解的 appKey，这里简单返回一个默认值
        return "DEFAULT_STRING_APP_KEY";
    }

    private String getAppKeyForListField() {
        // 从某处获取 List<String> 字段注解的 appKey，这里简单返回一个默认值
        return "DEFAULT_LIST_APP_KEY";
    }

    private String getAnswerFromChatGPT(String question, String appKey) {
        // 实现访问 ChatGPT API 并获取答案的逻辑
        // ...
        // ...
    }

    private Field getFieldWithValue(String value) {
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getType() == String.class) {
                try {
                    field.setAccessible(true);
                    if (field.get(this).equals(value)) {
                        return field;
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private void setFieldValue(Object object, Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(object, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
