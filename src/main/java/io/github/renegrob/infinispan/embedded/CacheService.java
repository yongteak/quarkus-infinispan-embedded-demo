package io.github.renegrob.infinispan.embedded;

import io.github.renegrob.infinispan.embedded.cdi.CacheListenerAdapter;
import io.github.renegrob.infinispan.embedded.cdi.CacheListenerCDIBridge;
import io.model.cache.NodeEntry;
import io.quarkus.runtime.ShutdownEvent;
import io.serializable.SerializableToJson;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import org.infinispan.Cache;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.manager.CacheManagerInfo;
import org.infinispan.manager.EmbeddedCacheManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@ApplicationScoped
@Slf4j
public class CacheService {

    private final EmbeddedCacheManager emc;
    private final Cache<String, String> cache;
    private final List<Object> cacheListeners = new ArrayList<>();

    @Inject
    CacheService(EmbeddedCacheManager emc, CacheListenerCDIBridge eventBridge) {
        emc.getCacheManagerInfo().getCacheNames().forEach(System.out::println);
        this.emc = emc;
        this.cache = emc.getCache();

        cacheListeners.add(new CacheListener());
        cacheListeners.add(new CacheListenerAdapter<>(eventBridge));
        cacheListeners.forEach(this.cache::addListener);
    }

    void onStop(@Observes ShutdownEvent ev) {
        cacheListeners.removeIf(listener -> {
            cache.removeListener(listener);
            return true;
        });
    }

    public void start() {
        emc.start();
        onBoot();
    }

    void onBoot() {
        Cache<String, String> cache = emc.getCache("NODE-ADDRESS-CACHE");
        long ts = System.currentTimeMillis();
        NodeEntry value = NodeEntry.builder()
                .connectedNodeCount(0)
                .restartCount(0)
                .nodeType("validator")
                .publicIp("127.0.0.1")
                .internalIp("127.0.0.1")
                .region("us-east-1")
                .status("running")
                .recvByte(0)
                .sendByte(0)
                .publicKey("publicKey")
                .startedAt(ts)
                .createdAt(ts)
                .updatedAt(ts)
                .build();
        String key = "node1";
        // String result = cache.get(name);

        // System.out.println("result = " + result);

        String existingValue = cache.get(key);

        log.info("existingValue = {}", existingValue);

        if (existingValue != null) {
            NodeEntry nodeEntry = NodeEntry.fromJson(existingValue);
            System.out.println("nodeEntry = " + nodeEntry);
        }
        // value.setRestartCount(0);
        cache.put(key, value.toJson());

        String newValue = cache.get(key);

        System.out.println("old = " + existingValue);
        System.out.println("new = " + newValue);

        // 비동기로 데이터 저장
        // CompletionStage<String> putFuture = cache.putAsync(key, value);

        // // 저장이 완료되면 실행
        // putFuture.thenAccept(previousValue -> {
        // System.out.println("old 값: " + previousValue);
        // // 저장 완료 후 데이터 조회
        // String newValue = cache.get(key);
        // System.out.println("new 값: " + newValue);
        // }).exceptionally(ex -> {
        // System.err.println("데이터 저장 중 오류 발생: " + ex.getMessage());
        // return null;
        // });
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
