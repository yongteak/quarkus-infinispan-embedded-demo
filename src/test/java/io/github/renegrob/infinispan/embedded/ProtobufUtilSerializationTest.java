package io.github.renegrob.infinispan.embedded;

import io.github.renegrob.infinispan.embedded.util.HexDump;
import org.infinispan.protostream.ProtobufUtil;
import org.infinispan.protostream.SerializationContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Instant;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class ProtobufUtilSerializationTest {

    private SerializationContext ctx;
    @BeforeEach
    void init() {
        MyProtoSchemaImpl schema = new MyProtoSchemaImpl();
        ctx = ProtobufUtil.newSerializationContext();
        schema.registerSchema(ctx);
        schema.registerMarshallers(ctx);
    }

    @Test
    void serializeMyCacheEntry() {
        Instant instant = Instant.ofEpochMilli(new Random().nextLong());
        String value = "abc";

        byte[] bytes = serialize(new MyCacheEntry(instant, value));
        System.out.println(new HexDump().toHexDump(bytes));
        Object result = deserialize(MyCacheEntry.class, bytes);
        assertThat(result).isInstanceOfSatisfying(MyCacheEntry.class, entry -> {
            assertThat(entry.getCreatedAt()).isEqualTo(instant);
            assertThat(entry.getValue()).isEqualTo(value);
        });
    }

    @Test
    void serializeMyCacheEntryProducer() {
        byte[] bytes = serialize(MyCacheEntryProducer.INSTANCE);
        System.out.println(new HexDump().toHexDump(bytes));
        Object result = deserialize(MyCacheEntryProducer.class, bytes);
        assertThat(result).isInstanceOfSatisfying(MyCacheEntryProducer.class, entry -> {
            assertThat(entry).isSameAs(MyCacheEntryProducer.INSTANCE);
        });
    }

    private <T> T deserialize(Class<T> clazz, byte[] bytes) {
        Object o;
        try {
            o = ProtobufUtil.fromWrappedByteArray(ctx, bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertThat(o).isInstanceOf(clazz);
        return clazz.cast(o);
    }

    private byte[] serialize(Object data) {
        try {
            return ProtobufUtil.toWrappedByteArray(ctx, data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
