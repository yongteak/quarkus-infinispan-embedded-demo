package io.github.renegrob.infinispan.embedded;

import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.infinispan.Cache;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.manager.CacheManagerInfo;
import org.infinispan.manager.EmbeddedCacheManager;

import java.util.Set;

/**
 * This startup bean starts the cache service
 */
// @Startup
// @Singleton
@ApplicationScoped
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

    public GlobalConfiguration getGlobalConfiguration() { return emc.getCacheManagerConfiguration(); }

    public Set<String> getCacheConfigurationNames() { return emc.getCacheConfigurationNames(); }
}
