package prog.rates.configurations;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Value("${service-configs.keycloak.url}")
    private String keyCloakUrl;
    @Value("${service-configs.keycloak.realm}")
    private String keyCloakRealm;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/convert").authenticated()
                        .anyRequest().permitAll()
                )
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt ->
                                jwt.decoder(jwtDecoder())
                        )
                );


        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        String uri = String.format("%s/realms/%s/protocol/openid-connect/certs", keyCloakUrl, keyCloakRealm);
        return NimbusJwtDecoder
                .withJwkSetUri(uri)
                .build();
    }
}

