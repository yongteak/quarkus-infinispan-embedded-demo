package io.route;

import org.apache.camel.builder.RouteBuilder;
import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class DamlWebsocketRoute extends RouteBuilder {

     static final Logger LOG = LoggerFactory.getLogger(DamlWebsocketRoute.class);

    //  @ConfigProperty(name = "ORACLIZER_HTTP_PORT", defaultValue = "8080")
    //  int httpPort;
     
    //  @ConfigProperty(name = "ORACLIZER_API_PORT", defaultValue = "8090")
    //  int apiPort;
    

    @Override
    public void configure() throws Exception {
        

    }
}
