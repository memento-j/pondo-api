package games.pondo.api.config;

import java.io.IOException;
import java.time.Duration;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    //one bucket per IP + method, forgotten after 10 minutes of inactivity
    private final Cache<String, Bucket> buckets = Caffeine.newBuilder()
            .expireAfterAccess(Duration.ofMinutes(10))
            .maximumSize(100_000)
            .build();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                    FilterChain chain) throws ServletException, IOException {
        if (!request.getRequestURI().startsWith("/api/")) {
            chain.doFilter(request, response);
            return;
        }

        boolean isPost = "POST".equals(request.getMethod());
        String key = request.getRemoteAddr() + ":" + request.getMethod();
        Bucket bucket = buckets.get(key, k -> newBucket(isPost ? 10 : 60));

        if (bucket.tryConsume(1)) {
            chain.doFilter(request, response);
        } else {
            response.setStatus(429); //too many requests
        }
    }

    //capacity requests per minute
    private Bucket newBucket(int perMinute) {
        return Bucket.builder()
                .addLimit(Bandwidth.builder()
                        .capacity(perMinute)
                        .refillGreedy(perMinute, Duration.ofMinutes(1))
                        .build())
                .build();
    }
}