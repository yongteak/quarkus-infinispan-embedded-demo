package io.github.renegrob.infinispan.embedded.cdi;

import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachelistener.annotation.*;
import org.infinispan.notifications.cachelistener.event.*;

/**
 * Infinispan 캐시 이벤트를 수신하여 CDI 브릿지를 통해 처리하는 리스너 어댑터.
 * 클러스터 환경에서 동기화되지 않은 이벤트 리스너로 동작합니다.
 * @Listener 어노테이션을 사용하여 Infinispan 캐시 이벤트 리스너로 등록
 *  => 리스너가 클러스터 환경에서 여러 인스턴스 간에 이벤트를 공유하면서도, 
 *  이벤트 처리를 위한 동기화 작업을 수행하지 않아 처리 성능을 향상시키는 방식으로 동작
 *  => 고성능이 요구되는 분산 시스템에서 유용할 수 있으나, 
 *  이벤트 처리 순서에 대한 엄격한 요구사항이 있는 경우 추가적인 고려가 필요할 수 있습니다.
 *  => sync = true시 모든 노드 동기화 보장
 */
@Listener(clustered = true, sync = false, primaryOnly = false)
public class CacheListenerAdapter<T> {

    private final CacheListenerCDIBridge<T> bridge;

    /**
     * 생성자.
     * 
     * @param bridge 캐시 이벤트를 처리할 CDI 브릿지
     */
    public CacheListenerAdapter(CacheListenerCDIBridge<T> bridge) {
        this.bridge = bridge;
    }

    /**
     * 캐시 항목이 생성될 때 호출됩니다.
     * 
     * @param event 생성된 캐시 항목의 이벤트 정보
     */
    @CacheEntryCreated
    public void entryCreated(CacheEntryCreatedEvent<String, T> event) {
        bridge.entryCreated(event);
    }

    /**
     * 캐시 항목이 수정될 때 호출됩니다.
     * 
     * @param event 수정된 캐시 항목의 이벤트 정보
     */
    @CacheEntryModified
    public void entryUpdated(CacheEntryModifiedEvent<String, T> event) {
        bridge.entryUpdated(event);
    }

    /**
     * 캐시 항목이 제거될 때 호출됩니다.
     * 
     * @param event 제거된 캐시 항목의 이벤트 정보
     */
    @CacheEntryRemoved
    public void entryRemoved(CacheEntryRemovedEvent<String, T> event) {
        bridge.entryRemoved(event);
    }

    /**
     * 캐시 항목이 만료될 때 호출됩니다.
     * 
     * @param event 만료된 캐시 항목의 이벤트 정보
     */
    @CacheEntryExpired
    public void entryExpired(CacheEntryExpiredEvent<String, T> event) {
        bridge.entryExpired(event);
    }

    /**
     * 하나 이상의 캐시 항목이 제거(evicted) 때 호출됩니다.
     * 
     * @param event 제거된 캐시 항목들의 이벤트 정보
     */
    @CacheEntriesEvicted
    public void entriesEvicted(CacheEntriesEvictedEvent<String, T> event) {
        bridge.entriesEvicted(event);
    }

}