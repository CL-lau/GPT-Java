package com.clau.gpt.annotation1;

import java.lang.reflect.Field;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class MyClass {

    @ChatGPTAnnotation(appKey = "YOUR_OPENAI_APPKEY")
    private String myQuestion = "What is the capital of France?";

    public MyClass() {
        // 找到带有 ChatGPTAnnotation 注解的字段并进行自动转换
        processAnnotations();
    }

    private void processAnnotations() {
        Class<?> clazz = this.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (field.getType() == String.class && field.isAnnotationPresent(ChatGPTAnnotation.class)) {
                try {
                    field.setAccessible(true);
                    String appKey = field.getAnnotation(ChatGPTAnnotation.class).appKey();
                    String originalString = (String) field.get(this);

                    // 访问 ChatGPT API 并获取答案
                    String answer = getAnswerFromChatGPT(originalString, appKey);

                    // 将答案重新赋值给字段
                    field.set(this, answer);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static final String GPT_API_URL = "https://api.openai.com/v1/engines/davinci-codex/completions";

    private String getAnswerFromChatGPT(String question, String appKey) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(GPT_API_URL))
                .header("Authorization", "Bearer " + appKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"prompt\": \"" + question + "\"}", StandardCharsets.UTF_8))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // 解析 JSON 响应并提取答案
            // Your JSON parsing code here...
            String answer = ... // 从 JSON 响应中提取答案
            return answer;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: Failed to get an answer from ChatGPT API";
        }
    }

    // ... Other methods ...
}
