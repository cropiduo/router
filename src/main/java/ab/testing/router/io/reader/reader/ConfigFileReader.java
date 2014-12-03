package ab.testing.router.io.reader.reader;

import ab.testing.router.exception.InitConfigurationException;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ConfigFileReader {

    private static Logger log = Logger.getLogger(ConfigFileReader.class);

    String configFilePath;

    @Autowired
    public ConfigFileReader(@Value("${route.config.file.path}") String configFilePath) {
        this.configFilePath = configFilePath;
    }

    public Map<String, AtomicInteger> readConfigFromFile() {
        HashMap<String, AtomicInteger> config;

        try {
            config = new ObjectMapper().readValue(ResourceUtils.getFile(configFilePath),
                    new TypeReference<HashMap<String, AtomicInteger>>() {
                    });
        } catch (IOException e) {
            log.error("Configuration cannot be read: " + e.getMessage());
            throw new InitConfigurationException("Configuration cannot be read: " + e.getMessage(), e);
        }

        return config;
    }
}
