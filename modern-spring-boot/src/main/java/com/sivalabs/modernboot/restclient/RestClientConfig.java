package com.sivalabs.modernboot.restclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
class RestClientConfig {

    @Bean
    RestClient restClient(@Value("${placeholder_api_base_uri}") String baseURI) {
        ClientHttpRequestFactory requestFactory =
                ClientHttpRequestFactories.get(ClientHttpRequestFactorySettings.DEFAULTS
                .withConnectTimeout(Duration.ofSeconds(5))
                .withReadTimeout(Duration.ofSeconds(5)));
        return RestClient
                .builder()
                .baseUrl(baseURI)
                .requestFactory(requestFactory)
                .defaultStatusHandler(HttpStatusCode::isError, (req, response) -> {
                    throw new RuntimeException("Error occurred while calling API: " + response.getStatusCode());
                })
                .build();
    }
}
