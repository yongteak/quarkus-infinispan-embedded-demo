package io.github.renegrob;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.infinispan.manager.EmbeddedCacheManager;

@Path("/hello")
public class GreetingResource {

    /**
     *  {@link org.infinispan.quarkus.embedded.runtime.InfinispanEmbeddedProducer produced by InfinispanEmbeddedProducer}
     */
    private final EmbeddedCacheManager emc;

    @Inject
    GreetingResource (EmbeddedCacheManager emc) {
        this.emc = emc;

    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello from RESTEasy Reactive";
    }
}
