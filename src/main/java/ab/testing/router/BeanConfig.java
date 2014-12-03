package ab.testing.router;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public RateLimiter rateLimiter(@Value("${rate.limiter.permits.per.second}") double permitsPerSecond) {
        return RateLimiter.create(permitsPerSecond);
    }
}
