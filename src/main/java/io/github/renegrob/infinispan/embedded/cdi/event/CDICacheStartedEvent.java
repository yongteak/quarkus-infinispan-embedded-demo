package io.github.renegrob.infinispan.embedded.cdi.event;

import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.notifications.cachemanagerlistener.event.CacheStartedEvent;

public class CDICacheStartedEvent implements CacheStartedEvent {
    private final CacheStartedEvent delegate;

    public CDICacheStartedEvent(CacheStartedEvent delegate) {
        this.delegate = delegate;
    }

    @Override
    public String getCacheName() {
        return delegate.getCacheName();
    }

    @Override
    public EmbeddedCacheManager getCacheManager() {
        return delegate.getCacheManager();
    }

    @Override
    public Type getType() {
        return delegate.getType();
    }
}
