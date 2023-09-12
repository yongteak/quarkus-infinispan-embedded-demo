package io.github.renegrob.infinispan.embedded;

import org.infinispan.protostream.annotations.ProtoFactory;
import org.infinispan.protostream.annotations.ProtoField;

import java.time.Instant;

public class MyCacheEntry {

    private Instant createdAt;

    @ProtoFactory
    public MyCacheEntry(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @ProtoField(number = 1)
    public Instant getCreatedAt() {
        return createdAt;
    }
}
