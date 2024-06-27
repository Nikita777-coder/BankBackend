package prog.rates.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
public class AppConfigurations {
    @Value("${service-configs.rates-service.path.base-url}")
    private String ratesServiceUrl;

    @Value("${service-configs.web-client.time-response}")
    private int responseTimeout;

    @Value("${service-configs.keycloak.url}")
    private String keyCloakUrl;
    @Bean
    public WebClient webClient() {
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(responseTimeout));

        return WebClient.builder()
                .baseUrl(ratesServiceUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultStatusHandler(HttpStatusCode::is5xxServerError,
                        (this::handleServerErrors))
                .defaultStatusHandler(HttpStatusCode::is4xxClientError,
                        (this::handleClientErrors))
                .build();
    }

    @Bean
    public WebClient keyCloakWebClient() {
        return WebClient.builder()
                .baseUrl(keyCloakUrl)
                .build();
    }
    private Mono<? extends Throwable> handleServerErrors(ClientResponse clientResponse) {
        throw new IllegalStateException(clientResponse.toString());
    }
    private Mono<? extends Throwable> handleClientErrors(ClientResponse clientResponse) {
        throw new IllegalStateException(clientResponse.toString());
    }
}
