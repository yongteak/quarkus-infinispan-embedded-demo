package io.github.renegrob.infinispan.embedded;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.infinispan.commons.marshall.WrappedByteArray;
import org.infinispan.encoding.DataConversion;
import org.infinispan.manager.EmbeddedCacheManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
public class SerializationTest {

    private DataConversion valueDataConversion;

    @Inject
    EmbeddedCacheManager emc;

    @BeforeEach
    void init() {
        valueDataConversion = emc.getCache().getAdvancedCache().getValueDataConversion();
        assertThat(valueDataConversion).describedAs("valueDataConversion").isNotNull();
    }

    @Test
    void serializeMyCacheEntry() {
        Instant instant = Instant.ofEpochMilli(new Random().nextLong());
        String value = "abc";

        byte[] bytes = serialize(new MyCacheEntry(instant, value));
        Object result = deserialize(MyCacheEntry.class, bytes);
        assertThat(result).isInstanceOfSatisfying(MyCacheEntry.class, entry -> {
            assertThat(entry.getCreatedAt()).isEqualTo(instant);
            assertThat(entry.getValue()).isEqualTo(value);
        });
    }

    @Test
    void serializeMyCacheEntryProducer() {
        byte[] bytes = serialize(MyCacheEntryProducer.INSTANCE);
        Object result = deserialize(MyCacheEntryProducer.class, bytes);
        assertThat(result).isInstanceOfSatisfying(MyCacheEntryProducer.class, entry -> {
            assertThat(entry).isSameAs(MyCacheEntryProducer.INSTANCE);
        });
    }

    private <T> T deserialize(Class<T> clazz, byte[] bytes) {
        Object o = valueDataConversion.fromStorage(new WrappedByteArray(bytes));
        assertThat(o).isInstanceOf(clazz);
        return clazz.cast(o);
    }

    private byte[] serialize(Object data) {
        return ((WrappedByteArray) valueDataConversion.toStorage(data)).getBytes();
    }
}
