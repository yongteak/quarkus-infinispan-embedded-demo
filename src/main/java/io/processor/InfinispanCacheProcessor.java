package io.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.infinispan.Cache;

import io.github.renegrob.infinispan.embedded.CacheService;
import io.model.cache.DamlContractEntry;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class InfinispanCacheProcessor implements Processor {

    @Inject
    private CacheService cs;

    public void process(Exchange ex) throws Exception {
        Cache<String, String> cache = cs.getCache("DAML-CONTRACT-CACHE");
        DamlContractEntry entry = ex.getIn().getBody(DamlContractEntry.class);
        String contractId = entry.getContract_id();
        String hash = entry.getHash();

        // TODO
        // [] 중복 저장 방지를 위해 1~5초 랜덤 지연 코드 추가 [2024-04-19 12:51:39]
        String cacheValue = cache.get(entry.getContract_id());
        if (cacheValue != null) {
            log.info("Duplicate contract id");
            // throw new RuntimeException("Duplicate contract id");
        } else {
            cache.put(contractId, entry.toString());
        }
        

        // cache.put(contractId, entry.toString());

    }
}
