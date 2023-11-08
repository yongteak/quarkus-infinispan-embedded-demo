package io.github.renegrob.infinispan.embedded.cdi;

import io.github.renegrob.infinispan.embedded.MyCacheEntry;
import io.github.renegrob.infinispan.embedded.cdi.event.*;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.infinispan.notifications.cachelistener.event.*;

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

    public void entryCreated(CacheEntryCreatedEvent<String, MyCacheEntry> event) {
        cacheEntryCreatedEvent.fire(new CDICacheEntryCreatedEvent<>(event));
    }

    public void entryUpdated(CacheEntryModifiedEvent<String, MyCacheEntry> event) {
        cacheEntryModifiedEvent.fire(new CDICacheEntryModifiedEvent<>(event));
    }

    public void entryUpdated(CacheEntryRemovedEvent<String, MyCacheEntry> event) {
        cacheEntryRemovedEvent.fire(new CDICacheEntryRemovedEvent<>(event));
    }

    public void entryExpired(CacheEntryExpiredEvent<String, MyCacheEntry> event) {
        cacheEntryExpiredEvent.fire(new CDICacheEntryExpiredEvent<>(event));
    }

    public void entriesEvicted(CacheEntriesEvictedEvent<String, MyCacheEntry> event) {
        cacheEntriesEvictedEvent.fire(new CDICacheEntriesEvictedEvent<>(event));
    }
}