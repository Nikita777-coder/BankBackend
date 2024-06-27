package accounts.services.authservices;

import org.keycloak.representations.AccessTokenResponse;

public interface AuthService {
    AccessTokenResponse getAccessToken();
}
