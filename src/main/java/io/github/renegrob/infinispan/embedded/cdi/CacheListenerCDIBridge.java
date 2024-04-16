package io.github.renegrob.infinispan.embedded.cdi;

import io.github.renegrob.infinispan.embedded.MyCacheEntry;
import io.github.renegrob.infinispan.embedded.cdi.event.*;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.infinispan.notifications.cachelistener.event.*;

/**
 * Infinispan 캐시 이벤트를 CDI 이벤트로 변환하여 발행하는 클래스입니다.
 * 이 클래스는 캐시 이벤트를 감지하고, 해당 이벤트를 CDI 이벤트로 변환하여 애플리케이션의 다른 부분에서 사용할 수 있도록 합니다.
 */
@Singleton
public class CacheListenerCDIBridge {

    @Inject
    Event<CacheEntryCreatedEvent<String, MyCacheEntry>> cacheEntryCreatedEvent;

    @Inject
    Event<CacheEntryRemovedEvent<String, MyCacheEntry>> cacheEntryRemovedEvent;

    @Inject
    Event<CDICacheEntryExpiredEvent<String, MyCacheEntry>> cacheEntryExpiredEvent;

    @Inject
    Event<CDICacheEntriesEvictedEvent<String, MyCacheEntry>> cacheEntriesEvictedEvent;

    @Inject
    Event<CacheEntryModifiedEvent<String, MyCacheEntry>> cacheEntryModifiedEvent;

    /**
     * 캐시에 새로운 항목이 생성될 때 호출됩니다.
     * @param event 캐시 생성 이벤트
     */
    public void entryCreated(CacheEntryCreatedEvent<String, MyCacheEntry> event) {
        cacheEntryCreatedEvent.fire(new CDICacheEntryCreatedEvent<>(event));
    }

    /**
     * 캐시 항목이 수정될 때 호출됩니다.
     * @param event 캐시 수정 이벤트
     */
    public void entryUpdated(CacheEntryModifiedEvent<String, MyCacheEntry> event) {
        cacheEntryModifiedEvent.fire(new CDICacheEntryModifiedEvent<>(event));
    }

    /**
     * 캐시 항목이 제거될 때 호출됩니다.
     * @param event 캐시 제거 이벤트
     */
    public void entryUpdated(CacheEntryRemovedEvent<String, MyCacheEntry> event) {
        cacheEntryRemovedEvent.fire(new CDICacheEntryRemovedEvent<>(event));
    }

    /**
     * 캐시 항목이 만료될 때 호출됩니다.
     * @param event 캐시 만료 이벤트
     */
    public void entryExpired(CacheEntryExpiredEvent<String, MyCacheEntry> event) {
        cacheEntryExpiredEvent.fire(new CDICacheEntryExpiredEvent<>(event));
    }

    /**
     * 캐시에서 여러 항목이 추방될 때 호출됩니다.
     * @param event 캐시 추방 이벤트
     */
    public void entriesEvicted(CacheEntriesEvictedEvent<String, MyCacheEntry> event) {
        cacheEntriesEvictedEvent.fire(new CDICacheEntriesEvictedEvent<>(event));
    }
}