package com.clau.gpt.prompt.service;

import com.clau.gpt.annotation.client.ChatGptClient;
import com.clau.gpt.prompt.PromptInitializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Service
public class PromptService {

    @Autowired
    private PromptInitializer promptInitializer;

    @Autowired
    private ChatGptClient chatGptClient;

    public String getPrompt(String text) throws Exception {
//        promptInitializer.run(null);
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
//            System.out.println(String.valueOf(maxSimilarity) + maxSimilarity);
        }
        return mostSimilarWord;
    }

    private double[] parseEmbedding(String sentence) {
        List<Double> embeddings = chatGptClient.transEmbeddings(sentence, "", "");
        return embeddings.stream()
                .mapToDouble(Double::doubleValue)
                .toArray();
    }

    public double[] parseEmbedding(String sentence, @Nullable String appKey, @Nullable String openAiURL) throws IOException {
        List<Double> embeddings = chatGptClient.transEmbeddings(sentence, appKey, openAiURL);
        return embeddings.stream()
                .mapToDouble(Double::doubleValue)
                .toArray();
    }

    public List<double[]> batchGetEmbedding(List<String> sentences, @Nullable String appKey, @Nullable String openAiURL) throws IOException {
        List<double[]> embeddings = new ArrayList<>();
        for (int i = 0; i < sentences.size(); i++) {
            List<Double> embeddingList = chatGptClient.transEmbeddings(sentences.get(i), appKey, openAiURL);
            embeddings.add(embeddingList.stream().mapToDouble(Double::doubleValue).toArray());
        }
        return embeddings;
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

