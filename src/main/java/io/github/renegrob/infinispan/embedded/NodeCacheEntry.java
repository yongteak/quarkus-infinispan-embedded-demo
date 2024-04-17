package io.github.renegrob.infinispan.embedded;

import org.infinispan.protostream.annotations.ProtoFactory;
import org.infinispan.protostream.annotations.ProtoField;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class NodeCacheEntry {

    private final String key;
    private final String value;

    @ProtoFactory
    public NodeCacheEntry(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @ProtoField(number = 1, required = true)
    public String getKey() {
        return key;
    }

    @ProtoField(number = 2, required = true)
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "NodeCacheEntry{" +
                "key=" + key +
                ", value='" + value + '\'' +
                '}';
    }
}