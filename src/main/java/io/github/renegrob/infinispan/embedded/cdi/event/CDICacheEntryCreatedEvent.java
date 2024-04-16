package io.github.renegrob.infinispan.embedded.cdi.event;

import org.infinispan.Cache;
import org.infinispan.metadata.Metadata;
import org.infinispan.notifications.cachelistener.event.CacheEntryCreatedEvent;
import org.infinispan.transaction.xa.GlobalTransaction;

// Infinispan 캐시에서 캐시 항목이 생성될 때 발생하는 이벤트
public class CDICacheEntryCreatedEvent<K, V> implements CacheEntryCreatedEvent<K, V> {

    private final CacheEntryCreatedEvent<K, V> delegate;

    // 생성자
    public CDICacheEntryCreatedEvent(CacheEntryCreatedEvent<K, V> delegate) {
        this.delegate = delegate;
    }

    // 아래는 인터페이스의 메서드를 구현한 것입니다.

    // 키를 반환
    @Override
    public K getKey() {
        return delegate.getKey();
    }

    // 값 반환
    @Override
    public V getValue() {
        return delegate.getValue();
    }

    // 메타데이터 반환
    @Override
    public Metadata getMetadata() {
        return delegate.getMetadata();
    }

    // 명령이 재시도되었는지 여부 반환
    @Override
    public boolean isCommandRetried() {
        return delegate.isCommandRetried();
    }

    // 전역 트랜잭션 반환
    @Override
    public GlobalTransaction getGlobalTransaction() {
        return delegate.getGlobalTransaction();
    }

    // 원본이 로컬인지 여부 반환
    @Override
    public boolean isOriginLocal() {
        return delegate.isOriginLocal();
    }

    // 이벤트 유형 반환
    @Override
    public Type getType() {
        return delegate.getType();
    }

    // 이벤트가 사전인지 여부 반환
    @Override
    public boolean isPre() {
        return delegate.isPre();
    }

    // 캐시 반환
    @Override
    public Cache<K, V> getCache() {
        return delegate.getCache();
    }
}