package io.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.resource.spi.ConfigProperty;

@ApplicationScoped
public class RestConfiguration extends RouteBuilder {

    @ConfigProperty(name = "quarkus.camel.rest.port", defaultValue = "8088")
    int port;

    @Override
    public void configure() throws Exception {
        restConfiguration().component("netty-http")
            .contextPath("/v1/sdap/service")
            .port(port)
            .bindingMode(RestBindingMode.json)
            .jsonDataFormat("json-jackson").enableCORS(true)
            .dataFormatProperty("prettyPrint", "true");
    }
}
