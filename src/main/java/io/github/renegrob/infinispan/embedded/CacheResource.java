package io.github.renegrob.infinispan.embedded;

import io.github.renegrob.infinispan.embedded.cdi.CacheListenerAdapter;
import io.github.renegrob.infinispan.embedded.cdi.CacheListenerCDIBridge;
import io.quarkus.runtime.ShutdownEvent;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.infinispan.Cache;
import org.infinispan.manager.EmbeddedCacheManager;
import org.jboss.resteasy.reactive.RestPath;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Path("/cache")
public class CacheResource {

    private final Cache<String, MyCacheEntry> cache;
    private final List<Object> cacheListeners = new ArrayList<>();

    @Inject
    CacheResource(EmbeddedCacheManager emc, CacheListenerCDIBridge eventBridge) {
        this.cache = emc.getCache();

        cacheListeners.add(new CacheListener());
        cacheListeners.add(new CacheListenerAdapter(eventBridge));
        cacheListeners.forEach(this.cache::addListener);
    }

    void onStop(@Observes ShutdownEvent ev) {
        cacheListeners.removeIf(listener -> {
            cache.removeListener(listener);
            return true;
        });
    }

    @GET
    @Path("{key}")
    public CompletableFuture<MyCacheEntry> cache(@RestPath String key) {
        return cache.computeIfAbsentAsync(key, MyCacheEntryProducer.INSTANCE, 10, TimeUnit.SECONDS);
    }
}
