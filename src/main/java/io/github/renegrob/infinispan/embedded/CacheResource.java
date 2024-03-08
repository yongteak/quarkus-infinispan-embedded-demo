package io.github.renegrob.infinispan.embedded;

// @Path("/cache")
public class CacheResource {

//     private final Cache<String, MyCacheEntry> cache;
//     private final List<Object> cacheListeners = new ArrayList<>();

//     @Inject
//     CacheResource(EmbeddedCacheManager emc, CacheListenerCDIBridge eventBridge) {
//         this.cache = emc.getCache();

//         cacheListeners.add(new CacheListener());
//         cacheListeners.add(new CacheListenerAdapter(eventBridge));
//         cacheListeners.forEach(this.cache::addListener);
//     }

//     void onStop(@Observes ShutdownEvent ev) {
//         cacheListeners.removeIf(listener -> {
//             cache.removeListener(listener);
//             return true;
//         });
//     }

//     // @GET
// // @Path("{key}")
// // /**
// //  * 데이터가 캐시에 없는 경우 캐시에 추가한 후 결과값 반환.
// //  * 비동기 처리 결과를 로깅하고, 클라이언트에는 키를 반환한다.
// //  * @param key 캐시에서 조회하거나 추가할 키
// //  * @return CompletableFuture로 감싼 키
// //  */
// // public CompletableFuture<MyCacheEntry> cache(@RestPath String key) {
// //     return cache.computeIfAbsentAsync(key, MyCacheEntryProducer.INSTANCE)
// //     .thenApply(myCacheEntry -> {
// //         // 비동기 작업이 성공적으로 완료된 후 실행될 로직
// //         // 여기서는 성공 로그를 남기고, 키를 반환
// //         System.out.println("Cache entry successfully processed for key: " + key);
// //         return myCacheEntry;
// //     })
// //     .exceptionally(ex -> {
// //         // 비동기 작업 중 예외가 발생한 경우 실행될 로직
// //         System.err.println("Error processing cache entry for key: " + key + ", error: " + ex.getMessage());
// //         return new MyCacheEntry(null,null);
// //     });
// // }

//     // @GET
//     // @Path("{key}")
//     // public CompletableFuture<MyCacheEntry> cache(@RestPath String key) {
//     //     return cache.computeIfAbsentAsync(key, MyCacheEntryProducer.INSTANCE, 10, TimeUnit.SECONDS);
//     // }
}
