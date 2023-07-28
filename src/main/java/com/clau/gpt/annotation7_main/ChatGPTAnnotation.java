package com.clau.gpt.annotation7_main;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ChatGPTAnnotation {
    String appKey();
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ChatGPTListAnnotation {
    String appKey();
}

