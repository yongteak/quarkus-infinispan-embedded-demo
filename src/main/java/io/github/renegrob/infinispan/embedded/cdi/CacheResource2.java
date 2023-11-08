package io.github.renegrob.infinispan.embedded.cdi;

import io.github.renegrob.infinispan.embedded.MyCacheEntry;
import io.github.renegrob.infinispan.embedded.MyCacheEntryProducer;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.infinispan.Cache;
import org.jboss.resteasy.reactive.RestPath;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Path("/cache2")
public class CacheResource2 {

    private final Cache<String, MyCacheEntry> cache;

    @Inject
    CacheResource2(@Embedded(name = "cache2", template = "myDistributedCacheTemplate")
                   Cache<String, MyCacheEntry> cache) {
        this.cache = cache;
    }

    @GET
    @Path("{key}")
    public CompletableFuture<MyCacheEntry> cache(@RestPath String key) {
        return cache.computeIfAbsentAsync(key, MyCacheEntryProducer.INSTANCE, 10, TimeUnit.SECONDS);
    }
}
