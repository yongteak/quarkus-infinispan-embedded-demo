package io.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.TransportConfiguration;
import org.infinispan.remoting.transport.jgroups.JGroupsTransport;
import org.jgroups.JChannel;
import org.jgroups.protocols.TP;
import org.jgroups.stack.ProtocolStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.renegrob.infinispan.embedded.CacheService;
import io.model.cache.MyEntry;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;

import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class SystemRoute extends RouteBuilder {

    static final Logger LOG = LoggerFactory.getLogger(SystemRoute.class);

    @Inject
    CacheService cacheService;

    @Override
    public void configure() throws Exception {
        JacksonDataFormat jacksonDataFormat = new JacksonDataFormat();
        jacksonDataFormat.addModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        jacksonDataFormat.setUnmarshalType(MyEntry.class);

        restConfiguration().component("netty-http");
        // 캐시에 데이터를 추가하는 엔드포인트
        rest("/system")
                .consumes(MediaType.APPLICATION_JSON)
                .produces(MediaType.APPLICATION_JSON)
                // .post("/{key}").to("direct:createEntry")
                // .get("/{key}").to("direct:readEntry")
                // .put("/{key}").to("direct:updateEntry")
                // .delete("/{key}").to("direct:deleteEntry");
                .get("/network").to("direct:network");

        from("direct:network")
                .process(exchange -> {
                    GlobalConfiguration globalConfig = cacheService.getGlobalConfiguration();
                    TransportConfiguration transport = globalConfig.transport();
                    // JChannel channel = (JChannel) transport.jgroups().
                    ProtocolStack stack = ((JGroupsTransport) transport.jgroups().transport()).getChannel().getProtocolStack();
                    TP transportProtocol = stack.getTransport();
                    int currentPort = transportProtocol.getBindPort();
                    exchange.getMessage().setBody(
                        Map.of(
                            "bind_port",currentPort,
                            "cache_info",cacheService.getCacheManagerInfo().toJson()));
                })
                // JSON으로 변환하기 위해 Camel의 JacksonDataFormat을 사용합니다.
                .marshal().json(JsonLibrary.Jackson);
    }
}
