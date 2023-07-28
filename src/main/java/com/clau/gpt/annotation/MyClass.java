package com.clau.gpt.annotation;

import com.clau.gpt.annotation.processor.ChatGPTAnnotationProcessor;

public class MyClass {
    @ChatGPTAnnotation(appKey = "YOUR_OPENAI_APPKEY")
    private String myQuestion = "What is the capital of France?";

    // ... Other fields and methods ...

    public static void main(String[] args) {
        MyClass myClass = new MyClass();
        ChatGPTAnnotationProcessor.processAnnotations(myClass);

        // Now the 'myQuestion' field in 'myClass' object should have the answer from ChatGPT API.
        System.out.println(myClass.myQuestion); // Output: Paris
    }
}
