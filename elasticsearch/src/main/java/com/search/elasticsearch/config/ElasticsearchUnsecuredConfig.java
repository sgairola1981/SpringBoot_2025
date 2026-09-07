package com.search.elasticsearch.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

@Configuration
@ConditionalOnProperty(name = "elk.security-enabled", havingValue = "false")
@RequiredArgsConstructor
public class ElasticsearchUnsecuredConfig extends ElasticsearchConfiguration {

    private final ElasticsearchProperties elkProperties;

    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo(elkProperties.getHost())
                .build();
    }
}