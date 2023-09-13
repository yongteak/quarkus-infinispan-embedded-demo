package io.github.renegrob.infinispan.embedded;

import java.time.Instant;

public class MyCacheEntryProducer implements java.util.function.Function<String, MyCacheEntry> {

    public static final MyCacheEntryProducer INSTANCE = new MyCacheEntryProducer();

    private MyCacheEntryProducer() {
    }

    @Override
    public MyCacheEntry apply(String key) {
        // Here you could load the entry from the database or do some expensive operation
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
        return new MyCacheEntry(Instant.now(), String.format("%s produced on %s", key, System.getProperty("io.github.renegrob.infinispan.node")));
    }
}
