package io.model.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.serializable.SerializableToJson;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class NodeEntry implements SerializableToJson {
    private String nodeType;
    private String publicIp;
    private String internalIp;
    private String region;
    private String status;
    private long recvByte;
    private long sendByte;
    private int restartCount;
    private int connectedNodeCount;
    private String publicKey;
    private long startedAt;
    private long createdAt;
    private long updatedAt;

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