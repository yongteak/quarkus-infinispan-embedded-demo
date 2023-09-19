package io.github.renegrob.infinispan.embedded;

import io.quarkus.arc.Arc;
import org.infinispan.manager.EmbeddedCacheManager;

import java.time.Instant;
import java.util.function.Function;

public class MyCacheEntryProducer implements Function<String, MyCacheEntry> {

    public static final MyCacheEntryProducer INSTANCE = new MyCacheEntryProducer();

    private MyCacheEntryProducer() {
    }

    private EmbeddedCacheManager getCacheService() {
        return Arc.container().instance(EmbeddedCacheManager.class).get();
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
        return new MyCacheEntry(Instant.now(), String.format("%s produced on %s", key, getCacheService().getCacheManagerInfo().getNodeName()));
    }
}
