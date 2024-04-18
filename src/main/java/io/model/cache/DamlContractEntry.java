package io.model.cache;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.serializable.SerializableToJson;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class DamlContractEntry implements SerializableToJson {
    String contract_id;
    String hash;
    JsonNode contract;
    String event;

    @Builder.Default
    String host = "daml-node-1.asia";
    @Builder.Default
    int confirm = 1;
    @Builder.Default
    long created_at = System.currentTimeMillis();
    @Builder.Default
    long updated_at = System.currentTimeMillis();

    @Override
    public String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            throw new RuntimeException("JSON 변환 실패", e);
        }
    }

    public static NodeEntry fromJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, NodeEntry.class);
        } catch (Exception e) {
            throw new RuntimeException("JSON에서 NodeEntry 객체로 변환 실패", e);
        }
    }
}