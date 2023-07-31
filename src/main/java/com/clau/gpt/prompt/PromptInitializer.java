package com.clau.gpt.prompt;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Setter
@Getter
@Component
public class PromptInitializer implements ApplicationRunner {
    private Map<String, double[]> embeddingsMap = new HashMap<>();
    private Map<String, String> actsMap = new HashMap<>();

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, Object>> dataList = objectMapper.readValue(new File("com/clau/gpt/prompt/prompts-zh.json"), new TypeReference<List<Map<String, Object>>>() {});
        for (Map<String, Object> data : dataList) {
            String word = (String) data.get("act");
            double[] embedding = objectMapper.convertValue(data.get("embedding"), double[].class);
            embeddingsMap.put(word, embedding);
        }

        List<Map<String, Object>> actList = objectMapper.readValue(new File("com/clau/gpt/prompt/prompts-zh.json"), new TypeReference<List<Map<String, Object>>>() {});
        for (Map<String, Object> data : actList) {
            String word = (String) data.get("act");
            String prompt = (String) data.get("prompt");
            actsMap.put(word, prompt);
        }
    }
}
