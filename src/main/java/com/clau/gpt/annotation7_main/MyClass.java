package com.clau.gpt.annotation7_main;

public class MyClass {

    @ChatGPTAnnotation(appKey = "YOUR_OPENAI_APPKEY")
    private String myQuestion = "What is the capital of France?";

    @ChatGPTListAnnotation(appKey = "YOUR_OPENAI_APPKEY")
    private List<String> myList = Arrays.asList("How are you?", "What time is it?");

    // ... Other fields and methods ...
}

