package com.clau.gpt.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ChatGPTAnnotation {
    String appKey();
}
