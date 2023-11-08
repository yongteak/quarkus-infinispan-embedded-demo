package io.github.renegrob.infinispan.embedded.cdi.event;

import org.infinispan.Cache;
import org.infinispan.metadata.Metadata;
import org.infinispan.notifications.cachelistener.event.CacheEntryRemovedEvent;
import org.infinispan.transaction.xa.GlobalTransaction;

public class CDICacheEntryRemovedEvent<K, V> implements CacheEntryRemovedEvent<K, V> {

    private final CacheEntryRemovedEvent<K, V> delegate;

    public CDICacheEntryRemovedEvent(CacheEntryRemovedEvent<K, V> delegate) {
        this.delegate = delegate;
    }

    @Override
    public K getKey() {
        return delegate.getKey();
    }

    @Override
    public V getValue() {
        return delegate.getValue();
    }

    @Override
    public V getOldValue() {
        return delegate.getOldValue();
    }

    @Override
    public Metadata getOldMetadata() {
        return delegate.getOldMetadata();
    }

    @Override
    public Metadata getMetadata() {
        return delegate.getMetadata();
    }

    @Override
    public boolean isCommandRetried() {
        return delegate.isCommandRetried();
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
