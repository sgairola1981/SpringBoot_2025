package com.search.elasticsearch.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "elk")
@Getter
@Setter
public class ElasticsearchProperties {
    private boolean securityEnabled;
    private String host;
    private String username;
    private String password;
}