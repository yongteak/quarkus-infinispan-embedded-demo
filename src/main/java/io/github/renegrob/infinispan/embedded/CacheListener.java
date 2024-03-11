package io.github.renegrob.infinispan.embedded;

import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryCreated;
import org.infinispan.notifications.cachelistener.event.CacheEntryCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Listener(clustered = true, sync = false, primaryOnly = false)
/**
 * 이벤트 리스너
 * 캐시의 라이프 타임 이벤트 확인
 */
public class CacheListener {

    private static final Logger LOG = LoggerFactory.getLogger(CacheListener.class);

    @CacheEntryCreated
    public void entryCreated(CacheEntryCreatedEvent<String, MyCacheEntry> event) {
        LOG.info("-- [생성] {} with value {} created", event.getKey(), event.getValue());
    }
}