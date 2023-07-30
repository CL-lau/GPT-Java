package com.clau.gpt.annotation.processor;

import com.clau.gpt.annotation.ChatGptQuestion;
import com.clau.gpt.annotation.client.ChatGptClient;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Component
public class ChatGptAnnotationProcessor {
    private final ChatGptClient chatGptClient;

    public ChatGptAnnotationProcessor(ChatGptClient chatGptClient) {
        this.chatGptClient = chatGptClient;
    }

    public void process(Object object) throws IllegalAccessException {
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(ChatGptQuestion.class)) {
                field.setAccessible(true);
                ChatGptQuestion annotation = field.getAnnotation(ChatGptQuestion.class);
                String appKey = annotation.appKey();
                String endpoint = annotation.endpoint();
                if (field.get(object) == null) {
                    continue; // Skip if the field value is null
                }
                if (field.getType().equals(String.class)) {
                    String question = (String) field.get(object);
                    if (question.isEmpty()) {
                        continue; // Skip if the String question is empty
                    }
                    String answer = chatGptClient.sendMessage(question, appKey, endpoint);
                    field.set(object, answer);
                } else if (field.getType().equals(List.class)) {
                    List<String> questions = (List<String>) field.get(object);
                    List<String> answers = new ArrayList<>();
                    if (questions.isEmpty()) {
                        continue; // Skip if the List of questions is empty
                    }
                    if (answers == null) {
                        answers = new ArrayList<>();
                    }
                    for (String question : questions) {
                        String answer = chatGptClient.sendMessage(question, appKey, endpoint);
                        answers.add(answer);
                    }
                    // If new questions are added, get the GPT answers and add them to the list
                    if (answers.size() < questions.size()) {
                        for (int i = answers.size(); i < questions.size(); i++) {
                            String answer = chatGptClient.sendMessage(questions.get(i), appKey, endpoint);
                            answers.add(answer);
                        }
                    }
                    field.set(object, answers);
                }
            }
        }
    }
}






//import com.clau.gpt.annotation.ChatGptQuestion;
//import com.clau.gpt.annotation.client.ChatGptClient;
//import com.clau.gpt.annotation.service.ChatService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.lang.reflect.Field;
//import java.util.*;
//
//@Component
//public class ChatGptAnnotationProcessor {
//    private static final Set<String> processedFields = new HashSet<>();
//
//    @Autowired
//    private static ChatGptClient chatGptClient;
//
//    public ChatGptAnnotationProcessor(ChatGptClient chatGptClient) {
//        this.chatGptClient = chatGptClient;
//    }
//    public static void process(Object obj) {
//        Class<?> clazz = obj.getClass();
//        Field[] fields = clazz.getDeclaredFields();
//
//        for (Field field : fields) {
//            if (field.isAnnotationPresent(ChatGptQuestion.class)) {
//                field.setAccessible(true);
//                try {
//                    String fieldName = field.getName();
//                    if (processedFields.contains(fieldName)) {
//                        continue; // Skip if the field has been processed already
//                    }
//
//                    Object fieldValue = field.get(obj);
//                    if (fieldValue == null) {
//                        continue; // Skip if the field value is null
//                    }
//
//                    ChatGptQuestion annotation = field.getAnnotation(ChatGptQuestion.class);
//                    String appKey = annotation.appKey();
//                    String apiUrl = annotation.endpoint();
//
//                    if (fieldValue instanceof String) {
//                        String question = (String) fieldValue;
//                        if (question.isEmpty()) {
//                            continue; // Skip if the String question is empty
//                        }
//                        String answer = chatGptClient.sendMessage(question, appKey, apiUrl);
//                        field.set(obj, answer);
//                    } else if (fieldValue instanceof List) {
//                        List<String> questions = (List<String>) fieldValue;
//                        if (questions.isEmpty()) {
//                            continue; // Skip if the List of questions is empty
//                        }
//
//                        List<String> answers = (List<String>) field.get(obj);
//                        if (answers == null) {
//                            answers = new ArrayList<>();
//                        }
//
//                        // If new questions are added, get the GPT answers and add them to the list
//                        if (answers.size() < questions.size()) {
//                            for (int i = answers.size(); i < questions.size(); i++) {
//                                String answer = chatGptClient.sendMessage(questions.get(i), appKey, apiUrl);
//                                answers.add(answer);
//                            }
//                        }
//                        field.set(obj, answers);
//                    }
//
//                    // Mark the field as processed
//                    processedFields.add(fieldName);
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    public static void reprocess(Object obj, String fieldName) {
//        if (processedFields.contains(fieldName)) {
//            processedFields.remove(fieldName);
//            process(obj);
//
//            // Restore the original value after reprocessing new values
//            if (obj instanceof ChatService) {
//                ChatService chatService = (ChatService) obj;
//                Map<String, String> originalValues = chatService.getOriginalValues();
//                if (originalValues.containsKey(fieldName)) {
//                    try {
//                        Field field = obj.getClass().getDeclaredField(fieldName);
//                        field.setAccessible(true);
//                        field.set(obj, originalValues.get(fieldName));
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//    }
//}
//
//
//
//
