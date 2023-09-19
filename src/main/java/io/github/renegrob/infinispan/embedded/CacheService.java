package io.github.renegrob.infinispan.embedded;

import io.quarkus.runtime.Startup;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.infinispan.Cache;
import org.infinispan.manager.CacheManagerInfo;
import org.infinispan.manager.EmbeddedCacheManager;

/**
 * This startup bean starts the cache service
 */
@Startup
@Singleton
public class CacheService {

    /**
     *  {@link org.infinispan.quarkus.embedded.runtime.InfinispanEmbeddedProducer produced by InfinispanEmbeddedProducer}
     */
    private final EmbeddedCacheManager emc;
    private final Cache<String, MyCacheEntry> cache;

    @Inject
    CacheService(EmbeddedCacheManager emc) {
        this.emc = emc;
        this.cache = emc.getCache();
    }

    public CacheManagerInfo getCacheManagerInfo() {
        return emc.getCacheManagerInfo();
    }
}
