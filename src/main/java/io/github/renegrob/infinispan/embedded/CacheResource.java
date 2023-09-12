package io.github.renegrob.infinispan.embedded;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.infinispan.Cache;
import org.infinispan.manager.EmbeddedCacheManager;
import org.jboss.resteasy.reactive.RestPath;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Path("/cache")
public class CacheResource {

    /**
     *  {@link org.infinispan.quarkus.embedded.runtime.InfinispanEmbeddedProducer produced by InfinispanEmbeddedProducer}
     */
    private final EmbeddedCacheManager emc;
    private final Cache<String, MyCacheEntry> cache;

    @Inject
    CacheResource(EmbeddedCacheManager emc) {
        this.emc = emc;
        this.cache = emc.getCache();
    }

    @GET
    @Path("{key}")
    public MyCacheEntry cache(@RestPath String key) {
       // MyCacheEntry result = cache.computeIfAbsent(key, this::produceCacheEntry);
        cache.putIfAbsent(key, createCacheEntry(key), 30, TimeUnit.SECONDS);
        MyCacheEntry result = cache.get(key);
        return result;
    }

    private MyCacheEntry createCacheEntry(String key) {
        MyCacheEntry myCacheEntry = new MyCacheEntry(Instant.now());
        return myCacheEntry;
    }

}
