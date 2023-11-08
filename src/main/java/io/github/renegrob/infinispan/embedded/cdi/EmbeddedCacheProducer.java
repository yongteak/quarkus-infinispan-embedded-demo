package io.github.renegrob.infinispan.embedded.cdi;

import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.inject.Inject;
import org.infinispan.Cache;
import org.infinispan.commons.api.CacheContainerAdmin.AdminFlag;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachemanagerlistener.annotation.CacheStarted;
import org.infinispan.notifications.cachemanagerlistener.event.CacheStartedEvent;

public class EmbeddedCacheProducer {

    private final EmbeddedCacheManager emc;

    @Inject
    EmbeddedCacheProducer(EmbeddedCacheManager emc) {
        this.emc = emc;
        this.emc.addListener(new CacheEventListener());
    }

    @Produces
    @Embedded
    <K, V> Cache<K,V> produceCache(InjectionPoint ip) {
        Embedded embedded = ip.getAnnotated().getAnnotation(Embedded.class);
        String name = embedded.name();
        if (name.isEmpty()) {
            return emc.getCache();
        } else {
            String template = embedded.template();
            AdminFlag adminFlag = embedded.adminFlag();
            return emc.administration().withFlags(adminFlag).getOrCreateCache(name, template);
        }
    }

    @Listener
    private static class CacheEventListener {
        @CacheStarted
        public void cacheStarted(CacheStartedEvent event) {
            //event.getCacheName()
        }
    }
}
