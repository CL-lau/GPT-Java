package com.clau.gpt.annotation3;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ChatGPTAnnotation {
    String appKey();
}
