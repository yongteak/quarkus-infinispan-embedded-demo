package io.model.daml;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class DAMLResponse {
    private int status;
    private DefaultDAMLResult result;
}