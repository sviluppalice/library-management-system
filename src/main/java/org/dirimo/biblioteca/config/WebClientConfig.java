package org.dirimo.biblioteca.config;

import io.netty.channel.ChannelOption;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder()
                .baseUrl("http://localhost:8080")  // Base URL
                .clientConnector(new ReactorClientHttpConnector(createHttpClient()));
    }

    private HttpClient createHttpClient() {
        return HttpClient.create()
                .responseTimeout(Duration.ofSeconds(10))  // Set the response timeout
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000); // Connection timeout
    }
}
