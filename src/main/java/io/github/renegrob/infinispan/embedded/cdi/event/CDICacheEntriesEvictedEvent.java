package io.github.renegrob.infinispan.embedded.cdi.event;

import org.infinispan.Cache;
import org.infinispan.notifications.cachelistener.event.CacheEntriesEvictedEvent;

import java.util.Map;

/**
 * Infinispan 캐시에서 항목이 제거될 때 발생하는 이벤트를 CDI 환경에서 사용하기 위한 래퍼 클래스입니다.
 * 이 클래스는 Infinispan의 {@link CacheEntriesEvictedEvent}를 구현하며, 이벤트에 대한 세부 정보를 제공합니다.
 *
 * @param <K> 캐시 키의 타입
 * @param <V> 캐시 값의 타입
 */
public class CDICacheEntriesEvictedEvent<K, V> implements CacheEntriesEvictedEvent<K, V> {
    // delegate 이벤트 객체
    private final CacheEntriesEvictedEvent<K, V> delegate;

    /**
     * CDICacheEntriesEvictedEvent 생성자.
     *
     * @param delegate 실제 이벤트 정보를 담고 있는 Infinispan의 CacheEntriesEvictedEvent 객체
     */
    public CDICacheEntriesEvictedEvent(CacheEntriesEvictedEvent<K, V> delegate) {
        this.delegate = delegate;
    }

    /**
     * 제거된 항목들을 반환합니다.
     *
     * @return 제거된 항목들의 맵. 키는 캐시의 키, 값은 캐시에서 제거된 값입니다.
     */
    public Map<K, V> getEntries() {
        return delegate.getEntries();
    }

    /**
     * 이벤트의 타입을 반환합니다.
     *
     * @return 이벤트 타입.
     */
    @Override
    public Type getType() {
        return delegate.getType();
    }

    /**
     * 이벤트가 발생하기 전인지 여부를 반환합니다.
     *
     * @return true면 이벤트가 발생하기 전, false면 이벤트가 발생한 후입니다.
     */
    @Override
    public boolean isPre() {
        return delegate.isPre();
    }

    /**
     * 이 이벤트와 관련된 캐시를 반환합니다.
     *
     * @return 이 이벤트와 관련된 캐시 객체.
     */
    @Override
    public Cache<K, V> getCache() {
        return delegate.getCache();
    }
}
