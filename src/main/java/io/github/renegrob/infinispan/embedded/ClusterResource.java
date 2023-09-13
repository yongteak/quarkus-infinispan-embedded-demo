package io.github.renegrob.infinispan.embedded;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.infinispan.commons.dataconversion.internal.Json;

import java.util.List;

@Path("/cluster")
public class ClusterResource {

    private final CacheService cacheService;

    @Inject
    ClusterResource(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @GET
    public Json info() {
        return cacheService.getCacheManagerInfo().toJson();
    }


    @GET
    @Path("members")
    public List<String> members() {
        return cacheService.getCacheManagerInfo().getClusterMembersPhysicalAddresses();
    }
}
