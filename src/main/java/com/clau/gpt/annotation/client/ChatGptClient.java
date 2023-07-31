package com.clau.gpt.annotation.client;

import com.clau.gpt.ChatGPT;
import com.clau.gpt.util.Proxys;
import org.springframework.stereotype.Component;

import java.net.Proxy;
import java.util.List;

@Component
public class ChatGptClient {

    public static Proxy proxy = Proxy.NO_PROXY;
    public static String OPENAI_API_KEY;
    public static String OPENAI_URL = "https://api.openai.com/";
    public ChatGptClient(){
        OPENAI_API_KEY = System.getenv("OPENAI_API_KEY");
        if (OPENAI_API_KEY != null) {
            System.out.println("Value of OPENAI_API_KEY: " + OPENAI_API_KEY);
        } else {
            System.out.println("OPENAI_API_KEY not found in environment variables.");
        }
    }
    public String sendMessage(String message, String appKey, String openAiUrl) {
        if (appKey == null | appKey.equals("")){
            appKey = OPENAI_API_KEY;
            if (appKey == null && appKey.equals("")){
                OPENAI_API_KEY = System.getenv("OPENAI_API_KEY");
                appKey = System.getenv("OPENAI_API_KEY");
            }
        }
        if (openAiUrl == null | openAiUrl.equals("")){
            openAiUrl = OPENAI_URL;
        }
        Proxy proxy = Proxys.http("127.0.0.1", 7890);

        ChatGPT chatGPT = ChatGPT.builder()
                .apiKey(appKey)
                .proxy(proxy)
                .apiHost(openAiUrl)
                .build()
                .init();

        String res = "answer";
//        String res = chatGPT.chat(message);
        System.out.println(res);
        return res;
    }

    public List<Double> transEmbeddings(String text, String appKey, String openAiUrl) {
        if (appKey == null | appKey.equals("")){
            appKey = OPENAI_API_KEY;
            if (appKey == null && appKey.equals("")){
                OPENAI_API_KEY = System.getenv("OPENAI_API_KEY");
                appKey = System.getenv("OPENAI_API_KEY");
            }
        }
        if (openAiUrl == null | openAiUrl.equals("")){
            openAiUrl = OPENAI_URL;
        }
        Proxy proxy = Proxys.http("127.0.0.1", 7890);

        ChatGPT chatGPT = ChatGPT.builder()
                .apiKey(appKey)
                .proxy(proxy)
                .apiHost(openAiUrl)
                .build()
                .init();

        List<Double> res = chatGPT.transEmbeddings(text);
        return res;
    }

}
