package locaflix.corporation.locaflix.application.auth;

import java.time.Instant;

public record TokenResponse(String token, Instant expiresAt) {
}
