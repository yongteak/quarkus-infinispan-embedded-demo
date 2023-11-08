package io.github.renegrob.infinispan.embedded.cdi;

import io.github.renegrob.infinispan.embedded.MyCacheEntry;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Singleton;
import org.infinispan.notifications.cachelistener.event.*;
import org.infinispan.notifications.cachemanagerlistener.event.CacheStartedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class CdiCacheListener {
    static final Logger LOG = LoggerFactory.getLogger(CdiCacheListener.class);

    void entryCreated(@Observes CacheEntryCreatedEvent<String, MyCacheEntry> event) {
         LOG.info("-- Entry for {} with value {} created", event.getKey(), event.getValue());
    }

    void entryUpdated(@Observes CacheEntryModifiedEvent<String, MyCacheEntry> event){
        LOG.info("-- Entry for {} modified", event.getKey());
    }

    void cacheStarted(@Observes CacheStartedEvent event) {
        LOG.info("-- Cache {} started", event.getCacheName());
    }

    void entryRemovedFromCache(@Observes CacheEntryRemovedEvent event) {
        LOG.info("-- Entry for {} removed", event.getKey());
    }

    void entryExpired(@Observes CacheEntryExpiredEvent event) {
        LOG.info("-- Entry for {} expired", event.getKey());
    }

    void entriesEvictedFromCache(@Observes CacheEntriesEvictedEvent event) {
        LOG.info("-- Entries {} evicted", event.getEntries());
    }
}
