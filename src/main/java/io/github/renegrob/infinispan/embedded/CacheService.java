package io.github.renegrob.infinispan.embedded;

import io.github.renegrob.infinispan.embedded.cdi.CacheListenerAdapter;
import io.github.renegrob.infinispan.embedded.cdi.CacheListenerCDIBridge;
import io.model.cache.MyEntry;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.Startup;
import io.serializable.SerializableToJson;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.infinispan.Cache;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.manager.CacheManagerInfo;
import org.infinispan.manager.EmbeddedCacheManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

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
    private final Cache<String, String> cache;
    private final List<Object> cacheListeners = new ArrayList<>();

    @Inject
    CacheService(EmbeddedCacheManager emc,CacheListenerCDIBridge eventBridge) {
        this.emc = emc;
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

    public CacheManagerInfo getCacheManagerInfo() {
        return emc.getCacheManagerInfo();
    }

    public GlobalConfiguration getGlobalConfiguration() { return emc.getCacheManagerConfiguration(); }

    public Set<String> getCacheConfigurationNames() { return emc.getCacheConfigurationNames(); }

    // public CompletableFuture<MyCacheEntry> cache(String key) {
    //     return cache.computeIfAbsentAsync(key, MyCacheEntryProducer.INSTANCE, 10, TimeUnit.SECONDS);
    // }

    // public MyCacheEntry createEntry(String key) {
    //     return cache.computeIfAbsent(key, MyCacheEntryProducer.INSTANCE, 10, TimeUnit.SECONDS);
    //     // return cache.put(key, value);
    // }
    
    public String readEntry(String key) {
        return cache.get(key);
    }
    
    public String updateEntry(String key, SerializableToJson value) {
        String updatedEntry = cache.put(key, value.toJson());
        return updatedEntry;
    }
    // public MyEntry updateEntry(String key, MyEntry value) {
    //     return cache.put(key, new MyEntry(value.getKey(),value.getValue()));
    // }
    
    public String deleteEntry(String key) {
        return cache.remove(key);
    }
}
