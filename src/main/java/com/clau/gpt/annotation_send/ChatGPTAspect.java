package com.clau.gpt.annotation_send;
//
//<dependency>
//<groupId>org.apache.httpcomponents</groupId>
//<artifactId>httpclient</artifactId>
//<version>4.5.13</version>
//</dependency>
//<dependency>
//<groupId>com.fasterxml.jackson.core</groupId>
//<artifactId>jackson-core</artifactId>
//<version>2.12.5</version>
//</dependency>
//<dependency>
//<groupId>com.fasterxml.jackson.core</groupId>
//<artifactId>jackson-databind</artifactId>
//<version>2.12.5</version>
//</dependency>


public class ChatGPTAspect {
    import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

    private String getAnswerFromChatGPTAPI(String question, String appKey) {
        String apiUrl = "https://api.openai.com/v1/engines/davinci-codex/completions";
        String authorizationHeader = "Bearer " + appKey;
        String contentType = "application/json";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(apiUrl);
        httpPost.addHeader("Authorization", authorizationHeader);
        httpPost.addHeader("Content-Type", contentType);

        try {
            StringEntity body = new StringEntity("{\"prompt\": \"" + question + "\"}");
            httpPost.setEntity(body);

            CloseableHttpResponse response = httpClient.execute(httpPost);
            String responseBody = EntityUtils.toString(response.getEntity());
            return parseResponseForAnswer(responseBody);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: Failed to get an answer from ChatGPT API";
        }
    }
    import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

    private String parseResponseForAnswer(String responseBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            JsonNode choicesNode = jsonNode.get("choices");
            if (choicesNode.isArray() && choicesNode.size() > 0) {
                JsonNode answerNode = choicesNode.get(0).get("text");
                if (answerNode != null) {
                    return answerNode.asText();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Error: Failed to parse ChatGPT API response";
    }


}
