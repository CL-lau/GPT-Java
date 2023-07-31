package com.clau.gpt.prompt.service;

import com.clau.gpt.prompt.PromptInitializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

@Service
public class PromptService {

    @Autowired
    private PromptInitializer promptInitializer;

    public String getMostSimilarWord(String inputEmbedding) throws IOException {
        double[] input = parseEmbedding(inputEmbedding);
        String mostSimilarWord = null;
        double maxSimilarity = -1;
        for (Map.Entry<String, double[]> entry : promptInitializer.getEmbeddingsMap().entrySet()) {
            double similarity = cosineSimilarity(input, entry.getValue());
            if (similarity > maxSimilarity) {
                maxSimilarity = similarity;
                mostSimilarWord = entry.getKey();
            }
        }
        return mostSimilarWord;
    }

    public double[] parseEmbedding(String sentence) throws IOException {
        String url = "http://localhost:port/encoder/embedding";
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        String requestBody = "{\"text\":\"" + sentence + "\"}";
        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(requestBody.getBytes());
        outputStream.flush();
        outputStream.close();
        if (connection.getResponseCode() == 200) {
            InputStream inputStream = connection.getInputStream();
            byte[] bytes = inputStream.readAllBytes();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(bytes);
            return objectMapper.convertValue(jsonNode.get("embedding"), double[].class);
        } else {
            throw new RuntimeException("Failed to parse embedding for sentence: " + sentence);
        }
    }


    private double cosineSimilarity(double[] vectorA, double[] vectorB) {
        // 计算cosine相似度
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += Math.pow(vectorA[i], 2);
            normB += Math.pow(vectorB[i], 2);
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}

