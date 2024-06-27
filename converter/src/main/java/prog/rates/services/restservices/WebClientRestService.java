package prog.rates.services.restservices;

import lombok.RequiredArgsConstructor;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import prog.rates.services.configquerieservices.AuthService;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class WebClientRestService implements RestService {
    private final WebClient webClient;
    private final AuthService keyCloakAuthService;

    @Value("${web-retry.max-attempts}")
    private int maxAttempts;
    @Value("${web-retry.backoff.min-delay}")
    private long minDelay;
    @Value("${web-retry.backoff.max-delay}")
    private long maxDelay;
    @Override
    public <T> T get(String uri, Class<T> typeKey) {
        AccessTokenResponse authResponse = keyCloakAuthService.getAccessToken();

        Mono<T> response = webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path(uri).build())
                .header("Authorization", "Bearer " + authResponse.getToken())
                .retrieve()
                .bodyToMono(typeKey)
                .retryWhen(Retry.backoff(maxAttempts, Duration.ofMillis(minDelay))
                        .maxBackoff(Duration.ofMillis(maxDelay)));

        return response.block();
    }
}
