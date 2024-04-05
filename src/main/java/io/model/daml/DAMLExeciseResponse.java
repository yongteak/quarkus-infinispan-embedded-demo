package io.model.daml;

import lombok.Data;

@Data
public class DAMLExeciseResponse {
    private int status;
    private DAMLEventResult result;
}