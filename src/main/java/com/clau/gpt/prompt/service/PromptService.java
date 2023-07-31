package com.clau.gpt.prompt.service;

import com.clau.gpt.annotation.client.ChatGptClient;
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
import java.util.List;
import java.util.Map;

@Service
public class PromptService {

    @Autowired
    private PromptInitializer promptInitializer;

    @Autowired
    private ChatGptClient chatGptClient;

    public String getPrompt(String text) throws IOException {
        String mostSimilarWord = this.getMostSimilarWord(text);
        String prompt = promptInitializer.getActsMap().get(mostSimilarWord);
        return prompt;
    }

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
        List<Double> embeddings = chatGptClient.transEmbeddings(sentence, "", "");
        return embeddings.stream()
                .mapToDouble(Double::doubleValue)
                .toArray();
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

