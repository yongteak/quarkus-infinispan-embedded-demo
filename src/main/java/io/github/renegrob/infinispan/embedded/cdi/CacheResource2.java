package io.github.renegrob.infinispan.embedded.cdi;

// @Path("/cache2")
public class CacheResource2 {

    // private final Cache<String, MyCacheEntry> cache;

    // @Inject
    // CacheResource2(@Embedded(name = "cache2", template = "myDistributedCacheTemplate")
    //                Cache<String, MyCacheEntry> cache) {
    //     this.cache = cache;
    // }

    // @GET
    // @Path("{key}")
    // public CompletableFuture<MyCacheEntry> cache(@RestPath String key) {
    //     return cache.computeIfAbsentAsync(key, MyCacheEntryProducer.INSTANCE, 10, TimeUnit.SECONDS);
    // }
}
