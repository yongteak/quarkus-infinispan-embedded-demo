package io.model.daml;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;

@Data
public class DAMLFetchResponse {
    private int status;
    private DefaultDAMLResult result;
    private JsonNode originalData;
}