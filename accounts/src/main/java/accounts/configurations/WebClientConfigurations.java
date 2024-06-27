package accounts.configurations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
@Slf4j
public class WebClientConfigurations {
    @Value("${services-configs.customer-service.path.base-url}")
    private String converterServiceUrl;

    @Value("${services-configs.web-client.time-response}")
    private int responseTimeout;

    @Value("${services-configs.keycloak.url}")
    private String keyCloakUrl;
    @Bean
    public WebClient webClient() {
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(responseTimeout));
        log.info("HttpClient Created for WebClient");

        WebClient webClient = WebClient.builder()
                .baseUrl(converterServiceUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultStatusHandler(HttpStatusCode::is5xxServerError,
                        (this::handleServerErrors))
                .defaultStatusHandler(HttpStatusCode::is4xxClientError,
                        (this::handleClientErrors))
                .build();
        log.info("WebClient created");

        return webClient;
    }

    @Bean
    public WebClient keyCloakWebClient() {
        WebClient webClient = WebClient.builder()
                .baseUrl(keyCloakUrl)
                .build();
        log.info("keyCloak WebClient created");

        return webClient;
    }
    private Mono<? extends Throwable> handleServerErrors(ClientResponse clientResponse) {
        throw new IllegalStateException(clientResponse.toString());
    }
    private Mono<? extends Throwable> handleClientErrors(ClientResponse clientResponse) {
        throw new IllegalStateException(clientResponse.toString());
    }
}
