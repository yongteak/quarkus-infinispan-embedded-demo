package io.github.renegrob.infinispan.embedded;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.infinispan.Cache;
import org.infinispan.manager.EmbeddedCacheManager;
import org.jboss.resteasy.reactive.RestPath;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Path("/cache")
public class CacheResource {

    private final Cache<String, MyCacheEntry> cache;

    @Inject
    CacheResource(EmbeddedCacheManager emc) {
        this.cache = emc.getCache();
    }

    @GET
    @Path("{key}")
    public CompletableFuture<MyCacheEntry> cache(@RestPath String key) {
        return cache.computeIfAbsentAsync(key, MyCacheEntryProducer.INSTANCE, 10, TimeUnit.SECONDS);
    }
}
