package io.route;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.camel.Exchange;
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

     @ConfigProperty(name = "ORACLIZER_HTTP_PORT", defaultValue = "8080")
     int httpPort;
     
     @ConfigProperty(name = "ORACLIZER_API_PORT", defaultValue = "8090")
     int apiPort;

    @Inject
    CacheService cacheService;


    @Override
    public void configure() throws Exception {
        restConfiguration().component("netty-http")
            // .contextPath("/v1/sdap/service")
            .port(apiPort)
            .bindingMode(RestBindingMode.json)
            .jsonDataFormat("json-jackson").enableCORS(true)
            .dataFormatProperty("prettyPrint", "true");

        // REST 엔드포인트 정의
        rest("/api")
            .get("/hello").to("direct:hello");

        // 정적 웹 콘텐츠를 위한 netty-http 컴포넌트 설정
        from("netty-http:http://0.0.0.0:"+httpPort+"/?matchOnUriPrefix=true")
            .process(exchange -> {
                String path = exchange.getIn().getHeader(Exchange.HTTP_PATH, String.class);
                if (path == null || path.isEmpty() || path.equals("/") || !path.contains(".")) {
                    path = "index.html"; // 기본 페이지 설정
                }
                byte[] fileContent = Files.readAllBytes(Paths.get("src/main/resources/web", path));
                exchange.getMessage().setBody(fileContent);
                // 파일 확장자에 따라 적절한 Content-Type 설정
                String contentType = Files.probeContentType(Paths.get("src/main/resources/web", path));
                if (contentType != null) {
                    exchange.getMessage().setHeader(Exchange.CONTENT_TYPE, contentType);
                }
            });


        // API 엔드포인트 구현
        from("direct:hello")
            .setBody().constant("Hello from Camel!");

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
