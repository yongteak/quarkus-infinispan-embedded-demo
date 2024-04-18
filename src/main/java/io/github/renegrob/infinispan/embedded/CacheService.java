package io.github.renegrob.infinispan.embedded;

import io.github.renegrob.infinispan.embedded.cdi.CacheListenerAdapter;
import io.github.renegrob.infinispan.embedded.cdi.CacheListenerCDIBridge;
import io.model.cache.NodeEntry;
import io.quarkus.runtime.ShutdownEvent;
import io.serializable.SerializableToJson;
import io.vertx.mutiny.core.eventbus.EventBus;
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

    @Inject
    EventBus bus; 
    
    private final EmbeddedCacheManager emc;
    private final Cache<String, String> cache;
    private final List<Object> cacheListeners = new ArrayList<>();

    @Inject
    CacheService(EmbeddedCacheManager emc, CacheListenerCDIBridge eventBridge) {
        // emc.getCacheManagerInfo().getCacheNames().forEach(System.out::println);
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

    Boolean checkNodeMember() {
        // TODO
        // [] Member가 특정 숫자 이상인경우 연결이 정상적으로 되었다고 판단 [2024-04-18 15:22:34]
        return emc.getMembers().size() > 0;
    }

    void onBoot() {
        Cache<String, String> cache = emc.getCache("NODE-ADDRESS-CACHE");

        // emc.getMembers()
        long ts = System.currentTimeMillis();
        String clusterName = emc.getCacheManagerInfo().getClusterName();
        String nodeName = emc.getCacheManagerInfo().getNodeName();
        String key = clusterName + "-" + nodeName;
        // String result = cache.get(name);

        // System.out.println("result = " + result);

        String info = cache.get(key);
        if (info != null) {
            NodeEntry nodeEntry = NodeEntry.fromJson(info);
            nodeEntry.setRestartCount(nodeEntry.getRestartCount() + 1);
            nodeEntry.setUpdatedAt(ts);
            nodeEntry.setStartedAt(ts);
            cache.put(key, nodeEntry.toJson());
            log.info("update NodeEntry = {}", nodeEntry);
        } else {
            NodeEntry nodeEntry = NodeEntry.builder()
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
            cache.put(key, nodeEntry.toJson());
            log.info("create NodeEntry = {}", nodeEntry);
        }
        next();
    }

    // 네트워크/클러스터 연결 및 캐시 상태가 모두 확인 되었을 경우 
    void next() {
        bus.publish("daml-client-initialize", "initialize");
        // notifyListeners();
    }

    // private void notifyListeners() {
    //     log.info("002 CacheService : notifyListeners, size={}",listeners.size());
    //     for (ICacheServiceEventListener listener : listeners) {
    //         listener.onCacheServiceNext();
    //     }
    // }

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