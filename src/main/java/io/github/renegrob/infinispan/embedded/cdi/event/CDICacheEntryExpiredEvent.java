package io.github.renegrob.infinispan.embedded.cdi.event;

import org.infinispan.Cache;
import org.infinispan.metadata.Metadata;
import org.infinispan.notifications.cachelistener.event.CacheEntryExpiredEvent;
import org.infinispan.notifications.cachelistener.event.Event;
import org.infinispan.transaction.xa.GlobalTransaction;

public class CDICacheEntryExpiredEvent<K, V> implements CacheEntryExpiredEvent<K, V> {
    private final CacheEntryExpiredEvent<K, V> delegate;

    public CDICacheEntryExpiredEvent(CacheEntryExpiredEvent<K, V> delegate) {
        this.delegate = delegate;
    }

    public V getValue() {
        return delegate.getValue();
    }

    public K getKey() {
        return delegate.getKey();
    }

    public Metadata getMetadata() {
        return delegate.getMetadata();
    }

    public boolean isCurrentState() {
        return delegate.isCurrentState();
    }

    public Object getSource() {
        return delegate.getSource();
    }

    public GlobalTransaction getGlobalTransaction() {
        return delegate.getGlobalTransaction();
    }

    public boolean isOriginLocal() {
        return delegate.isOriginLocal();
    }

    public Event.Type getType() {
        return delegate.getType();
    }

    public boolean isPre() {
        return delegate.isPre();
    }

    public Cache<K, V> getCache() {
        return delegate.getCache();
    }
}
