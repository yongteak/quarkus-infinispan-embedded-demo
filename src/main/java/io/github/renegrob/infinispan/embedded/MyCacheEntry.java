package io.github.renegrob.infinispan.embedded;

import org.infinispan.protostream.annotations.ProtoFactory;
import org.infinispan.protostream.annotations.ProtoField;

import java.time.Instant;

// @Getter
// @Setter
// @AllArgsConstructor
public class MyCacheEntry {

    private final Instant createdAt;
    private final String value;

    @ProtoFactory
    public MyCacheEntry(Instant createdAt, String value) {
        this.createdAt = createdAt;
        this.value = value;
    }

    @ProtoField(number = 1, required = true)
    public Instant getCreatedAt() {
        return createdAt;
    }

    @ProtoField(number = 2, required = true)
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "MyCacheEntry{" +
                "createdAt=" + createdAt +
                ", value='" + value + '\'' +
                '}';
    }
}