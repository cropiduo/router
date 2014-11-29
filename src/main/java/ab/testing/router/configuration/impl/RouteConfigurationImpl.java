package ab.testing.router.configuration.impl;

import ab.testing.router.configuration.RouteConfiguration;
import ab.testing.router.exception.InitConfigurationException;
import com.google.common.collect.ImmutableMap;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public final class RouteConfigurationImpl implements RouteConfiguration {

    @Value("${route.config.file.path}")
    private String configFilePath;

    private Map<String, AtomicInteger> config;
    private int denominator;

    @PostConstruct
    public void initConfiguration() {
        config = new HashMap<>();

        try {
            config = new ObjectMapper().readValue(ResourceUtils.getFile(configFilePath),
                    new TypeReference<HashMap<String, AtomicInteger>>() {
                    });
        } catch (IOException e) {
            throw new InitConfigurationException("Configuration cannot be initialized: " + e.getMessage(), e);
        }

        denominator = config.values().stream().mapToInt(AtomicInteger::get).sum();
    }

    @Override
    public Map<String, AtomicInteger> getConfig() {
        return ImmutableMap.copyOf(config);
    }

    @Override
    public int getDenominator() {
        return denominator;
    }
}
