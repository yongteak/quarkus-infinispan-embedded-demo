package io.github.renegrob.infinispan.embedded;

import org.infinispan.protostream.annotations.ProtoFactory;
import org.infinispan.protostream.annotations.ProtoField;

import java.time.Instant;

public class MyCacheEntry {

    private Instant createdAt;
    private String value;

    @ProtoFactory
    public MyCacheEntry(Instant createdAt, String value) {
        this.createdAt = createdAt;
        this.value = value;
    }

    @ProtoField(number = 1)
    public Instant getCreatedAt() {
        return createdAt;
    }

    @ProtoField(number = 2)
    public String getValue() {
        return value;
    }
}
