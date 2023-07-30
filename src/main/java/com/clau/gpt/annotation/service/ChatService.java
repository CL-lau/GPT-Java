package com.clau.gpt.annotation.service;

import com.clau.gpt.annotation.ChatGptQuestion;
import com.clau.gpt.annotation.processor.ChatGptAnnotationProcessor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Component
public class ChatService {

    @Autowired
    private ChatGptAnnotationProcessor chatGptAnnotationProcessor;
    @ChatGptQuestion(appKey = "", endpoint = "")
    private String question = "What is the meaning of life?";

    @ChatGptQuestion(appKey = "", endpoint = "")
    private List<String> questions =  List.of(
            "What is the weather like today?",
            "Tell me a joke."
    );

//    @ChatGptQuestion(appKey = "", endpoint = "")
//    private String question;
//
//    @ChatGptQuestion(appKey = "", endpoint = "")
//    private List<String> questions;

    // Other methods and code for your service

    private final Map<String, String> originalValues = new HashMap<>();

    // Getters
    public String getQuestion() {
        return question;
    }

    public List<String> getQuestions() {
        return questions;
    }

    // Setters
    public void setQuestion(String question) throws IllegalAccessException {
        originalValues.put("question", question); // Store the original value
        this.question = question;
        chatGptAnnotationProcessor.process(this.question);

    }

    public void setQuestions(List<String> questions) throws IllegalAccessException {
        this.questions = questions;
        chatGptAnnotationProcessor.process(this.questions);
    }

    // Method to get original values map
    public Map<String, String> getOriginalValues() {
        return originalValues;
    }

}

