package io.route;

import org.apache.camel.builder.RouteBuilder;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class InfinispanCacheRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:cache-operator-daml-put")
        .log("InfinispanCacheRoute called");
    }
}
