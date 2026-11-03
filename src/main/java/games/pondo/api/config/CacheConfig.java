package games.pondo.api.config;

import com.github.benmanes.caffeine.cache.Caffeine;

import java.time.Duration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration 
@EnableCaching 
public class CacheConfig {
    //bean object spring creates and manages for me
    //bean method is how you tell it to build the object and you tell spring to manage it
    @Bean 
    public CacheManager cacheManager() {
        CaffeineCacheManager manager = new CaffeineCacheManager();

        manager.registerCustomCache("puzzleScores",
                Caffeine.newBuilder()
                        .expireAfterWrite(Duration.ofSeconds(60))
                        .maximumSize(500)
                        .build());

        return manager;
    }
}
