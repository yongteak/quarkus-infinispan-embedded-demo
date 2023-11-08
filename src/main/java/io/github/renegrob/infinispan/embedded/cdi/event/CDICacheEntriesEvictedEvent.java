package io.github.renegrob.infinispan.embedded.cdi.event;

import org.infinispan.Cache;
import org.infinispan.notifications.cachelistener.event.CacheEntriesEvictedEvent;

import java.util.Map;

public class CDICacheEntriesEvictedEvent<K, V> implements CacheEntriesEvictedEvent<K, V> {
    private final CacheEntriesEvictedEvent<K, V> delegate;

    public CDICacheEntriesEvictedEvent(CacheEntriesEvictedEvent<K, V> delegate) {
        this.delegate = delegate;
    }

    public Map<K, V> getEntries() {
        return delegate.getEntries();
    }

    @Override
    public Type getType() {
        return delegate.getType();
    }

    @Override
    public boolean isPre() {
        return delegate.isPre();
    }

    @Override
    public Cache<K, V> getCache() {
        return delegate.getCache();
    }
}
