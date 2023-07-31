package com.clau.gpt.prompt;
import com.clau.gpt.ChatGPT;
import com.clau.gpt.api.Api;
import com.clau.gpt.entity.billing.EmbeddingData;
import com.clau.gpt.entity.billing.EmbeddingRequest;
import com.clau.gpt.entity.billing.EmbeddingResponse;
import com.clau.gpt.util.Proxys;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.net.Proxy;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Replace "YOUR_API_KEY" with your actual API key
        String apiKey = System.getenv("OPENAI_API_KEY");
        Proxy proxy = Proxys.http("127.0.0.1", 7890);

        // Create an instance of ChatGPT
        ChatGPT chatGPT = ChatGPT.builder()
                .proxy(proxy)
                .apiKey(apiKey)
                .build()
                .init();

        // Sample text to get embeddings
        String text = "This is a sample text to get embeddings.";

        // Get embeddings for the sample text
        System.out.println(chatGPT.transEmbeddings(text));
//        List<EmbeddingData> embeddingsData = embeddingResponse.getData();

        // Process the embeddings data
//        System.out.println("Embeddings: " + embeddingsData);
    }
}
