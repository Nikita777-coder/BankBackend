package accounts.services.authservices;

import lombok.RequiredArgsConstructor;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class KeycloakAuthService implements AuthService {
    private final WebClient keyCloakWebClient;

    @Value("${services-configs.oauth.grand-type}")
    private String grandType;

    @Value("${services-configs.keycloak.realm}")
    private String realm;

    @Value("${services-configs.keycloak.client-id}")
    private String clientId;

    @Value("${services-configs.keycloak.client-secret}")
    private String clientSecret;
    @Override
    public AccessTokenResponse getAccessToken() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", grandType);
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);

        var response = keyCloakWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .pathSegment("realms", realm, "protocol", "openid-connect", "token")
                        .build())
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(AccessTokenResponse.class).block();

        if (response == null) {
            throw new IllegalArgumentException("response of getting access token is null");
        }

        return response;
    }
}
