package io.github.renegrob.infinispan.embedded.cdi;

import io.github.renegrob.infinispan.embedded.MyCacheEntry;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Singleton;
import org.infinispan.notifications.cachelistener.event.*;
import org.infinispan.notifications.cachemanagerlistener.event.CacheStartedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// @Singleton
/**
 * CDI로 구성된 모든 Listener데이터 수신
 */
public class CdiCacheListener {
    static final Logger LOG = LoggerFactory.getLogger(CdiCacheListener.class);

    void entryCreated(@Observes CacheEntryCreatedEvent<String, MyCacheEntry> event) {
         LOG.info("-- [created] Entry for {} with value {} created", event.getKey(), event.getValue());
    }

    void entryUpdated(@Observes CacheEntryModifiedEvent<String, MyCacheEntry> event){
        LOG.info("-- [modified] Entry for {} modified", event.getKey());
    }

    void cacheStarted(@Observes CacheStartedEvent event) {
        LOG.info("-- [started] Cache {} started", event.getCacheName());
    }

    void entryRemovedFromCache(@Observes CacheEntryRemovedEvent event) {
        LOG.info("-- [removed] Entry for {} removed", event.getKey());
    }

    void entryExpired(@Observes CacheEntryExpiredEvent event) {
        LOG.info("-- [expired] Entry for {} expired", event.getKey());
    }

    void entriesEvictedFromCache(@Observes CacheEntriesEvictedEvent event) {
        LOG.info("-- [evicted] Entries {} evicted", event.getEntries());
    }
}
