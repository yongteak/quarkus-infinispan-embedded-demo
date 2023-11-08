package io.github.renegrob.infinispan.embedded.cdi;

import io.github.renegrob.infinispan.embedded.MyCacheEntry;
import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachelistener.annotation.*;
import org.infinispan.notifications.cachelistener.event.*;

@Listener(clustered = true, sync = false, primaryOnly = false)
public class CacheListenerAdapter {

    private final CacheListenerCDIBridge bridge;

    public CacheListenerAdapter(CacheListenerCDIBridge bridge) {
        this.bridge = bridge;
    }

    @CacheEntryCreated
    public void entryCreated(CacheEntryCreatedEvent<String, MyCacheEntry> event) {
        bridge.entryCreated(event);
    }

    @CacheEntryModified
    public void entryUpdated(CacheEntryModifiedEvent<String, MyCacheEntry> event) {
        bridge.entryUpdated(event);
    }

    @CacheEntryRemoved
    public void entryUpdated(CacheEntryRemovedEvent<String, MyCacheEntry> event) {
        bridge.entryUpdated(event);
    }

    @CacheEntryExpired
    public void entryExpired(CacheEntryExpiredEvent<String, MyCacheEntry> event) {
        bridge.entryExpired(event);
    }

    @CacheEntriesEvicted
    public void entriesEvicted(CacheEntriesEvictedEvent<String, MyCacheEntry> event) {
        bridge.entriesEvicted(event);
    }

}