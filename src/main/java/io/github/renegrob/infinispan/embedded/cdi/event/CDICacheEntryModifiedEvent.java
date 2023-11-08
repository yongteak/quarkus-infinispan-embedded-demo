package io.github.renegrob.infinispan.embedded.cdi.event;

import org.infinispan.Cache;
import org.infinispan.metadata.Metadata;
import org.infinispan.notifications.cachelistener.event.CacheEntryModifiedEvent;
import org.infinispan.transaction.xa.GlobalTransaction;

public class CDICacheEntryModifiedEvent<K, V> implements CacheEntryModifiedEvent<K, V> {
    private final CacheEntryModifiedEvent<K, V> delegate;

    public CDICacheEntryModifiedEvent(CacheEntryModifiedEvent<K, V> delegate) {
        this.delegate = delegate;
    }

    @Override
    @Deprecated
    public V getValue() {
        return delegate.getValue();
    }

    @Override
    public V getOldValue() {
        return delegate.getOldValue();
    }

    @Override
    public V getNewValue() {
        return delegate.getNewValue();
    }

    @Override
    public Metadata getOldMetadata() {
        return delegate.getOldMetadata();
    }

    @Override
    public boolean isCreated() {
        return delegate.isCreated();
    }

    @Override
    public boolean isCommandRetried() {
        return delegate.isCommandRetried();
    }

    @Override
    public K getKey() {
        return delegate.getKey();
    }

    @Override
    public Metadata getMetadata() {
        return delegate.getMetadata();
    }

    @Override
    public boolean isCurrentState() {
        return delegate.isCurrentState();
    }

    @Override
    public Object getSource() {
        return delegate.getSource();
    }

    @Override
    public GlobalTransaction getGlobalTransaction() {
        return delegate.getGlobalTransaction();
    }

    @Override
    public boolean isOriginLocal() {
        return delegate.isOriginLocal();
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
