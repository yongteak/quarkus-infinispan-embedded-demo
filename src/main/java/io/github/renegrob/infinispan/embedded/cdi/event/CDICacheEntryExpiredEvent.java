package io.github.renegrob.infinispan.embedded.cdi.event;

import org.infinispan.Cache;
import org.infinispan.metadata.Metadata;
import org.infinispan.notifications.cachelistener.event.CacheEntryExpiredEvent;
import org.infinispan.notifications.cachelistener.event.Event;
import org.infinispan.transaction.xa.GlobalTransaction;

// 캐시 항목이 만료될 때 발생하는 이벤트
public class CDICacheEntryExpiredEvent<K, V> implements CacheEntryExpiredEvent<K, V> {
    private final CacheEntryExpiredEvent<K, V> delegate;

    // 생성자
    public CDICacheEntryExpiredEvent(CacheEntryExpiredEvent<K, V> delegate) {
        this.delegate = delegate;
    }

    // 값 가져오기
    public V getValue() {
        return delegate.getValue();
    }

    // 키 가져오기
    public K getKey() {
        return delegate.getKey();
    }

    // 메타데이터 가져오기
    public Metadata getMetadata() {
        return delegate.getMetadata();
    }

    // 현재 상태인지 확인
    public boolean isCurrentState() {
        return delegate.isCurrentState();
    }

    // 이벤트 소스 가져오기
    public Object getSource() {
        return delegate.getSource();
    }

    // 전역 트랜잭션 가져오기
    public GlobalTransaction getGlobalTransaction() {
        return delegate.getGlobalTransaction();
    }

    // 이벤트가 로컬에서 발생했는지 확인
    public boolean isOriginLocal() {
        return delegate.isOriginLocal();
    }

    // 이벤트 타입 가져오기
    public Event.Type getType() {
        return delegate.getType();
    }

    // 이벤트가 발생하기 전인지 확인
    public boolean isPre() {
        return delegate.isPre();
    }

    // 캐시 가져오기
    public Cache<K, V> getCache() {
        return delegate.getCache();
    }
}