package com.clau.gpt.entity.billing;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Setter
@Getter
public class EmbeddingData {
    private List<Double> embedding;
    private int index;
    private String object;

    // Getters and setters for the fields (if needed)
}

