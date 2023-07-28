package com.clau.gpt.annotation1;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ChatGPTAnnotation {
    String appKey();
}
