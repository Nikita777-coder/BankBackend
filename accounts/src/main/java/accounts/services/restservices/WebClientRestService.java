package accounts.services.restservices;

import accounts.services.authservices.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebClientRestService implements RestService {
    private final WebClient webClient;
    private final AuthService keycloakAuthService;
    @Override
    public <T> T get(String uri, Class<T> typeKey) {
        String token = getToken();

        Mono<T> response = webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path(uri).build())
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(typeKey);

        return response.block();
    }

    @Override
    public <T> T get(String uri, Map<String, Object> params, Class<T> typeKey) {
        String token = getToken();

        Mono<T> response = webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(uri)
                        .queryParam("from", params.get("from"))
                        .queryParam("to", params.get("to"))
                        .queryParam("amount", params.get("amount"))
                        .build()
                )
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(typeKey);

        return response.block();
    }

    private String getToken() {
        AccessTokenResponse authResponse = keycloakAuthService.getAccessToken();
        return authResponse.getToken();
    }
}
