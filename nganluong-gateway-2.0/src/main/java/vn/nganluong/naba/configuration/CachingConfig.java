//package vn.nganluong.naba.configuration;
//
//import org.springframework.cache.CacheManager;
//import org.springframework.cache.annotation.CachingConfigurer;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.cache.ehcache.EhCacheCacheManager;
//import org.springframework.cache.interceptor.CacheErrorHandler;
//import org.springframework.cache.interceptor.CacheResolver;
//import org.springframework.cache.interceptor.KeyGenerator;
//import org.springframework.cache.interceptor.SimpleCacheErrorHandler;
//import org.springframework.cache.interceptor.SimpleCacheResolver;
//import org.springframework.cache.interceptor.SimpleKeyGenerator;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import net.sf.ehcache.config.CacheConfiguration;
//
//@Configuration
//@EnableCaching
//public class CachingConfig implements CachingConfigurer {
//
//    public static final String USER_CACHE_INSTANCE = "my-spring-ehcache";
//    private final CacheManager cacheManager;
//    private final net.sf.ehcache.CacheManager ehCacheManager;
//
//
//    public CachingConfig() {
//        CacheConfiguration cacheConfiguration = new CacheConfiguration();
//        cacheConfiguration.setName(USER_CACHE_INSTANCE);
//        cacheConfiguration.setMemoryStoreEvictionPolicy("LRU");
//        cacheConfiguration.setMaxEntriesLocalHeap(1000);
//        net.sf.ehcache.config.Configuration config
//                = new net.sf.ehcache.config.Configuration();
//        config.addCache(cacheConfiguration);
//        this.ehCacheManager = net.sf.ehcache.CacheManager.newInstance(config);
//        this.cacheManager = new EhCacheCacheManager(ehCacheManager);
//    }
//
//    @Bean(destroyMethod="shutdown")
//    public net.sf.ehcache.CacheManager ehCacheManager() {
//        return ehCacheManager;
//    }
//
//    @Bean
//    @Override
//    public CacheManager cacheManager() {
//        return cacheManager;
//    }
//
//    @Bean
//    @Override
//    public KeyGenerator keyGenerator() {
//        return new SimpleKeyGenerator();
//    }
//
//    @Bean
//    @Override
//    public CacheResolver cacheResolver() {
//        return new SimpleCacheResolver(cacheManager);
//    }
//
//    @Bean
//    @Override
//    public CacheErrorHandler errorHandler() {
//        return new SimpleCacheErrorHandler();
//    }
//}