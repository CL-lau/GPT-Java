package com.clau.gpt.annotation4;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ChatGPTStringAnnotation {
    String appKey();
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ChatGPTListAnnotation {
    String appKey();
}

