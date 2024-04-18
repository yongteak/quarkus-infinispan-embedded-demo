package io.model.daml;

import lombok.Data;

@Data
public class DAMLFetchResponse {
    private int status;
    private DefaultDAMLResult result;
}