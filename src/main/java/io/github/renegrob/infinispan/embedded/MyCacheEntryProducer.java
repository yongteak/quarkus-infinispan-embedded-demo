package io.github.renegrob.infinispan.embedded;

import io.model.cache.MyEntry;
import io.quarkus.arc.Arc;
import org.infinispan.manager.EmbeddedCacheManager;

import java.time.Instant;
import java.util.function.Function;

public class MyCacheEntryProducer implements Function<String, MyEntry> {

    public static final MyCacheEntryProducer INSTANCE = new MyCacheEntryProducer();

    private MyCacheEntryProducer() {
    }

    private EmbeddedCacheManager getCacheService() {
        return Arc.container().instance(EmbeddedCacheManager.class).get();
    }

    @Override
    public MyEntry apply(String key) {
        // 여기에서는 데이터베이스에서 항목을 로드하거나 비용이 많이 드는 작업을 수행할 수 있습니다.
        // [2024-03-07 10:33:22] 캐시 데이터 Put
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
        return new MyEntry(key, String.format("%s produced on %s", key, getCacheService().getCacheManagerInfo().getNodeName()));
    }
}
