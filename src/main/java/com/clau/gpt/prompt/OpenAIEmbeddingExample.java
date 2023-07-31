package com.clau.gpt.prompt;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class OpenAIEmbeddingExample {

    public static void main(String[] args) {
        // Replace 'YOUR_API_KEY' with your actual API key from OpenAI
        String apiKey = System.getenv("OPEN_AI_KEY");

        // Text for which you want to get embeddings
        String text = "Hello, how are you?";

        // Construct the API endpoint URL
        String apiEndpoint = "https://api.openai.com/v1/embeddings";

        // Construct the HTTP client
        HttpClient httpClient = HttpClient.newHttpClient();

        // Construct the JSON payload
        String payload = "{\"input\":\"" + text + "\",\"model\":\"text-embedding-ada-002\"}";

        // Construct the HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiEndpoint))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();

        try {
            // Send the HTTP request and get the response
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Process the response
            if (response.statusCode() == 200) {
                System.out.println("Response: " + response.body());
                // Parse the response JSON to extract the embeddings
                // The structure of the response will depend on the specific OpenAI API endpoint used.
            } else {
                System.out.println("Error: " + response.statusCode());
                // Handle error responses
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
