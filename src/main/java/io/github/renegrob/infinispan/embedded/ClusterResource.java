package io.github.renegrob.infinispan.embedded;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.infinispan.commons.dataconversion.internal.Json;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.TransportConfiguration;
import org.infinispan.remoting.transport.jgroups.JGroupsTransport;
import org.jgroups.stack.ProtocolStack;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
    @Path("caches")
    public Set<?> caches() {
        return cacheService.getCacheManagerInfo().getDefinedCaches();
    }

    @GET
    @Path("members")
    public List<String> members() {
        return cacheService.getCacheManagerInfo().getClusterMembersPhysicalAddresses();
    }

    @GET
    @Path("config")
    public Map<String, Object> config() {
        GlobalConfiguration globalConfig = cacheService.getGlobalConfiguration();
        TransportConfiguration transport = globalConfig.transport();
        return Map.of("clustered", globalConfig.isClustered(),
                "transport.stack", transport.stack(),
                "globalState.enabled", globalConfig.globalState().enabled(),
                "globalState.storage", globalConfig.globalState().globalStorageConfiguration().configurationStorage().toString());

    }

    @GET
    @Path("protocols")
    public List<String> protocols() {
        GlobalConfiguration globalConfig = cacheService.getGlobalConfiguration();
        TransportConfiguration transport = globalConfig.transport();
        ProtocolStack stack = ((JGroupsTransport) transport.jgroups().transport()).getChannel().getProtocolStack();
        return stack.getProtocols().stream().map(Object::getClass).map(Class::getSimpleName).collect(Collectors.toList());
    }
}
