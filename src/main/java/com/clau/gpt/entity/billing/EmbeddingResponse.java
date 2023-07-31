package com.clau.gpt.entity.billing;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

//@Data
//public class EmbeddingResponse {
//    private List<Float> embeddings;
//    @JsonProperty("data")
//    private String data;
//
//    @JsonProperty("model")
//    private String model;
//
//    @JsonProperty("object")
//    private String object;
//
//    @JsonProperty("usage")
//    private String usage;
//
//    public List<Float> getEmbeddings() {
//        return embeddings;
//    }
//
//    // Getters and setters (if needed)
//}

import java.util.List;

@Data
@Setter
@Getter
public class EmbeddingResponse {
    @JsonProperty("data")
    private List<EmbeddingData> data;

    @JsonProperty("model")
    private String model;

    @JsonProperty("object")
    private String object;

    @JsonProperty("usage")
    private UsageStats usage;

    // Getters and setters for the fields (if needed)

    // Define a nested class for the 'usage' field
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    @Setter
    @Getter
    public static class UsageStats {
        private int prompt_tokens;
        private int total_tokens;

        // Getters and setters for the fields (if needed)
    }
}


