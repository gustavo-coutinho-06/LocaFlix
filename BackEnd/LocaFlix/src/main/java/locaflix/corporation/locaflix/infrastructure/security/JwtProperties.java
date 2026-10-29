package locaflix.corporation.locaflix.infrastructure.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "locaflix.jwt")
public record JwtProperties(String secret, Duration expiration) {
}
