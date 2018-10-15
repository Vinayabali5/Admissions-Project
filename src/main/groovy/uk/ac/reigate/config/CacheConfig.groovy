package uk.ac.reigate.config

import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.concurrent.ConcurrentMapCache
import org.springframework.cache.support.SimpleCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
@EnableCaching
class CachingConfig {
    
    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Arrays.asList(
                new ConcurrentMapCache("genders"),
                new ConcurrentMapCache("titles"),
                new ConcurrentMapCache("contactTypes"),
                new ConcurrentMapCache("nationalities"),
                new ConcurrentMapCache("ethnicities"),
                new ConcurrentMapCache("tutorGroups"),
                new ConcurrentMapCache("schools"),
                new ConcurrentMapCache("people"),
                new ConcurrentMapCache("staff"),
                new ConcurrentMapCache("offerTypes"),
                new ConcurrentMapCache("studentTypes"),
                //		new ConcurrentMapCache("students"),
                new ConcurrentMapCache("applicationForms")
                ));
        return cacheManager;
    }
}
