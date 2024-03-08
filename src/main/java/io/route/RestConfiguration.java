package io.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.renegrob.infinispan.embedded.CacheService;

@ApplicationScoped
public class RestConfiguration extends RouteBuilder {

     static final Logger LOG = LoggerFactory.getLogger(RestConfiguration.class);

    @ConfigProperty(name = "quarkus.camel.rest.port", defaultValue = "8088")
    int port;

    @Inject
    CacheService cacheService;


    @Override
    public void configure() throws Exception {
        restConfiguration().component("netty-http")
            // .contextPath("/v1/sdap/service")
            .port(port)
            .bindingMode(RestBindingMode.json)
            .jsonDataFormat("json-jackson").enableCORS(true)
            .dataFormatProperty("prettyPrint", "true");

        // ClusterResource의 엔드포인트를 Camel REST DSL로 정의합니다.
        rest("/cluster").produces("application/json")
            .get().to("direct:clusterInfo")
            .get("/caches").to("direct:caches")
            .get("/members").to("direct:members")
            .get("/config").to("direct:config");
            // .get("/protocols").to("direct:protocols");

        // 각 엔드포인트에 대한 로직을 구현합니다.
        // from("direct:clusterInfo")
        //     .process(exchange -> {
        //         exchange.getMessage().setBody(cacheService.getCacheManagerInfo().toJson());
        //     });
        from("direct:clusterInfo")
            .process(exchange -> {
                LOG.info(exchange.getIn().getMessageId());
                // JSON 객체를 문자열로 변환
                String json = cacheService.getCacheManagerInfo().toJson().toString();
                LOG.info(cacheService.getCacheManagerInfo().toJson().toString());
                exchange.getMessage().setBody("T");
            });

        from("direct:caches")
            .process(exchange -> {
                exchange.getMessage().setBody(cacheService.getCacheManagerInfo().getDefinedCaches());
            });

        from("direct:members")
            .process(exchange -> {
                exchange.getMessage().setBody(cacheService.getCacheManagerInfo().getClusterMembersPhysicalAddresses());
            });

        from("direct:config")
            .process(exchange -> {
                exchange.getMessage().setBody(cacheService.getGlobalConfiguration());
            });

        // from("direct:protocols")
        //     .process(exchange -> {
        //         exchange.getMessage().setBody(/* 여기에 protocols 로직을 구현합니다. */);
        //     });
    }
}
