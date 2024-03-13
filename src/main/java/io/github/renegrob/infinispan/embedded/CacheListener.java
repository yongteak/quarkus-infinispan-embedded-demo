package io.github.renegrob.infinispan.embedded;

import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryCreated;
import org.infinispan.notifications.cachelistener.event.CacheEntryCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// sync 속성은 이벤트 처리가 동기적으로 수행될지 비동기적으로 수행될지를 결정하는 것으로, 데이터 복제 방식과는 별개의 설정입니다.
// 설정은 항상 xml 구성에 의해서만 동작됨
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