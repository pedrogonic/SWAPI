package com.pedrogonic.swapi.application.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "cache")
@Data
public class CacheConfigurationProperties {

    private long timeoutSeconds = 60;
    private Map<String, Long> cacheExpiration = new HashMap<>();

}
