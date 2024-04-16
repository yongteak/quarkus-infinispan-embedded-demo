package io.github.renegrob.infinispan.embedded.cdi;

import io.github.renegrob.infinispan.embedded.cdi.event.*;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
// import jakarta.inject.Singleton;
import org.infinispan.notifications.cachelistener.event.*;

/**
 * Infinispan 캐시 이벤트를 CDI 이벤트로 변환하여 발행하는 제네릭 클래스입니다.
 * 이 클래스는 캐시 이벤트를 감지하고, 해당 이벤트를 CDI 이벤트로 변환하여 애플리케이션의 다른 부분에서 사용할 수 있도록 합니다.
 */
// @Singleton
@Dependent
public class CacheListenerCDIBridge<T> {

    @Inject
    Event<CacheEntryCreatedEvent<String, T>> cacheEntryCreatedEvent;

    @Inject
    Event<CacheEntryRemovedEvent<String, T>> cacheEntryRemovedEvent;

    @Inject
    Event<CDICacheEntryExpiredEvent<String, T>> cacheEntryExpiredEvent;

    @Inject
    Event<CDICacheEntriesEvictedEvent<String, T>> cacheEntriesEvictedEvent;

    @Inject
    Event<CacheEntryModifiedEvent<String, T>> cacheEntryModifiedEvent;

    public void entryCreated(CacheEntryCreatedEvent<String, T> event) {
        cacheEntryCreatedEvent.fire(new CDICacheEntryCreatedEvent<>(event));
    }

    public void entryUpdated(CacheEntryModifiedEvent<String, T> event) {
        cacheEntryModifiedEvent.fire(new CDICacheEntryModifiedEvent<>(event));
    }

    public void entryRemoved(CacheEntryRemovedEvent<String, T> event) {
        cacheEntryRemovedEvent.fire(new CDICacheEntryRemovedEvent<>(event));
    }

    public void entryExpired(CacheEntryExpiredEvent<String, T> event) {
        cacheEntryExpiredEvent.fire(new CDICacheEntryExpiredEvent<>(event));
    }

    public void entriesEvicted(CacheEntriesEvictedEvent<String, T> event) {
        cacheEntriesEvictedEvent.fire(new CDICacheEntriesEvictedEvent<>(event));
    }
}