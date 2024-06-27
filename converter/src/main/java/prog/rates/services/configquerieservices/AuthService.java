package prog.rates.services.configquerieservices;

import org.keycloak.representations.AccessTokenResponse;
import reactor.core.publisher.Mono;

public interface AuthService {
    AccessTokenResponse getAccessToken();
}
