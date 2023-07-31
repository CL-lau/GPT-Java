package com.clau.gpt.entity.billing;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmbeddingRequest {
    @JsonProperty("input")
    private String text;

    @JsonProperty("model")
    private String model = "text-embedding-ada-002";


    public EmbeddingRequest(String text) {
        this.text = text;
    }

    // You can add any other necessary fields and methods here
    // For example, if you need additional parameters for the embedding request.
}
