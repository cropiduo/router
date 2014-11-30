package ab.testing.router;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Value("${rate.limiter.permits.per.second}")
    private double permitsPerSecond;

    @Bean
    public RateLimiter rateLimiter() {
        return RateLimiter.create(permitsPerSecond);
    }
}
