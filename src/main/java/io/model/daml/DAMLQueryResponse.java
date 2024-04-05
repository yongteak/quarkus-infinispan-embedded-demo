package io.model.daml;

import java.util.List;

import lombok.Data;

@Data
public class DAMLQueryResponse {
    private int status;
    private List<DefaultDAMLResult> result;
}