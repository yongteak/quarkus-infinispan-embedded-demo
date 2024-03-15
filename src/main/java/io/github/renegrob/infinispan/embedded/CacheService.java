package io.github.renegrob.infinispan.embedded;

import io.github.renegrob.infinispan.embedded.cdi.CacheListenerAdapter;
import io.github.renegrob.infinispan.embedded.cdi.CacheListenerCDIBridge;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.serializable.SerializableToJson;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.infinispan.Cache;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.manager.CacheManagerInfo;
import org.infinispan.manager.EmbeddedCacheManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * This startup bean starts the cache service
 */
// @Startup
// @Singleton
@ApplicationScoped
public class CacheService {

    private final EmbeddedCacheManager emc;
    private final Cache<String, String> cache;
    private final List<Object> cacheListeners = new ArrayList<>();

    @Inject
    CacheService(EmbeddedCacheManager emc, CacheListenerCDIBridge eventBridge) {
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

    // void onStart(@Observes StartupEvent ev) {
    //     if (this.emc != null) {
    //         System.out.println("###### >>>> Infinispan 222");
    //         emc.start();
    //     } else {
    //         System.out.println("###### >>>> Infinispan 333");
    //     }
    // }

    // @PostConstruct
    // public void initialize() throws Exception {
    //     System.out.println("###### >>>> Infinispan 클라이언트가 시작됩니다.");
    //     emc.start();
    // }
    
    public void start() {
        emc.start();
    }

    public CacheManagerInfo getCacheManagerInfo() {
        return emc.getCacheManagerInfo();
    }

    public GlobalConfiguration getGlobalConfiguration() {
        return emc.getCacheManagerConfiguration();
    }

    public Set<String> getCacheConfigurationNames() {
        return emc.getCacheConfigurationNames();
    }

    // public CompletableFuture<MyCacheEntry> cache(String key) {
    // return cache.computeIfAbsentAsync(key, MyCacheEntryProducer.INSTANCE, 10,
    // TimeUnit.SECONDS);
    // }

    // public MyCacheEntry createEntry(String key) {
    // return cache.computeIfAbsent(key, MyCacheEntryProducer.INSTANCE, 10,
    // TimeUnit.SECONDS);
    // // return cache.put(key, value);
    // }

    public String readEntry(String key) {
        return cache.get(key);
    }

    public String updateEntry(String key, SerializableToJson value) {
        String updatedEntry = cache.put(key, value.toJson());
        return updatedEntry;
    }
    // public MyEntry updateEntry(String key, MyEntry value) {
    // return cache.put(key, new MyEntry(value.getKey(),value.getValue()));
    // }

    public String deleteEntry(String key) {
        return cache.remove(key);
    }
}
