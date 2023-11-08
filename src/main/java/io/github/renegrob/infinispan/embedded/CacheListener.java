package io.github.renegrob.infinispan.embedded;

import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryCreated;
import org.infinispan.notifications.cachelistener.event.CacheEntryCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Listener(clustered = true, sync = false, primaryOnly = false)
public class CacheListener {

    private static final Logger LOG = LoggerFactory.getLogger(CacheListener.class);

    @CacheEntryCreated
    public void entryCreated(CacheEntryCreatedEvent<String, MyCacheEntry> event) {
        LOG.info("-- Entry for {} with value {} created", event.getKey(), event.getValue());
    }
}