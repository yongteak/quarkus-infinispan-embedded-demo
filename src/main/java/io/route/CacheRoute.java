package io.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.renegrob.infinispan.embedded.CacheService;
import io.model.cache.MyEntry;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;

@ApplicationScoped
public class CacheRoute extends RouteBuilder {

     static final Logger LOG = LoggerFactory.getLogger(CacheRoute.class);

     @Inject
    CacheService cacheService;
     @Override
    public void configure() throws Exception {
        JacksonDataFormat jacksonDataFormat = new JacksonDataFormat();
        jacksonDataFormat.addModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        jacksonDataFormat.setUnmarshalType(MyEntry.class);


         restConfiguration().component("netty-http");
            // .contextPath("/v1/sdap/service")
            // .port(apiPort)
            // .bindingMode(RestBindingMode.auto)

            // .jsonDataFormat("json-jackson")
            // .enableCORS(true)
            // .dataFormatProperty("prettyPrint", "true");
        // 캐시에 데이터를 추가하는 엔드포인트
        rest("/cache")
        .consumes(MediaType.APPLICATION_JSON)
        .produces(MediaType.APPLICATION_JSON)
        // .post("/{key}").to("direct:createEntry")
        // .get("/{key}").to("direct:readEntry")
        // .put("/{key}").to("direct:updateEntry")
        // .delete("/{key}").to("direct:deleteEntry");
        .post("/create").to("direct:createEntry")
        .post("/read").to("direct:readEntry")
        .post("/update").to("direct:updateEntry")
        .post("/delete").to("direct:deleteEntry");

        rest("/hello")
        .get()
        .produces("application/json")
        .to("direct:helloWorld");
        
    from("direct:helloWorld")
    
        .setBody().constant("{\"message\": \"Hello, World!\"}");
        
    from("direct:createEntry")
        .log("Received create entry request with body: ${body}") // JSON 입력 로그
        // .marshal().json(JsonLibrary.Jackson)
        .unmarshal(jacksonDataFormat)
        .process(exchange -> {
            MyEntry entry = exchange.getIn().getBody(MyEntry.class);
            // MyCacheEntry cacheEntry = new MyCacheEntry(entry.getKey(), entry);
            System.out.println("## cacheEntry >>>> " + entry.toJson());
            cacheService.updateEntry(entry.getKey(), entry);
            exchange.getMessage().setBody(entry.toJson());
        });
        // .log("Created entry with value: ${header.value},result:${header.result} and value: ${body}"); // 처리 결과 로그;

    from("direct:readEntry")
        .log("run => direct:readEntry") // JSON 입력 로그
        .unmarshal(jacksonDataFormat)
        .process(exchange -> {
            MyEntry entry = exchange.getIn().getBody(MyEntry.class);
            String result = cacheService.readEntry(entry.getKey());
            // String key = exchange.getIn().getHeader("key", String.class);
            // MyCacheEntry result = cacheService.readEntry(key);
            exchange.getMessage().setBody(result);
        });

    from("direct:updateEntry")
        .unmarshal(jacksonDataFormat)
        .process(exchange -> {
            // MyEntry entry = exchange.getIn().getBody(MyEntry.class);
            // MyCacheEntry cacheEntry = new MyCacheEntry(entry.getKey(), entry.getValue());
            // cacheService.updateEntry(entry.getKey(), entry.getValue());
            // exchange.getMessage().setBody("ok");
        });

    from("direct:deleteEntry")
        .unmarshal(jacksonDataFormat)
        .process(exchange -> {
            // String key = exchange.getIn().getHeader("key", String.class);
            // String result = cacheService.deleteEntry(key);
            // exchange.getMessage().setBody(result);
        });
    }
}
