package com.clau.gpt.annotation.processor;

import com.clau.gpt.annotation.ChatGPTAnnotation;

import java.lang.reflect.Field;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.Map;

public class ChatGPTAnnotationProcessor {

    private static final String GPT_API_URL = "https://api.openai.com/v1/engines/davinci-codex/completions";

    private static String getAnswerFromChatGPT(String question, String appKey) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(GPT_API_URL))
                .header("Authorization", "Bearer " + appKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"prompt\": \"" + question + "\"}", StandardCharsets.UTF_8))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // Parse the JSON response to extract the answer
            // Your JSON parsing code here...
            String answer = "..."; // Extract the answer from the JSON response
            return answer;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: Failed to get an answer from ChatGPT API";
        }
    }

    public static void processAnnotations(Object obj) {
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (field.getType() == String.class && field.isAnnotationPresent(ChatGPTAnnotation.class)) {
                try {
                    field.setAccessible(true);
                    String appKey = field.getAnnotation(ChatGPTAnnotation.class).appKey();
                    String originalString = (String) field.get(obj);

                    // Access the ChatGPT API and get the answer
                    String answer = getAnswerFromChatGPT(originalString, appKey);

                    // Set the answer back to the field
                    field.set(obj, answer);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
