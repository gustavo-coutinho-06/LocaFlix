package locaflix.corporation.locaflix.domain.model;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public record Session(UUID id, UUID userId, Instant expiresAt) {

    public Session {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        if (userId == null) {
            throw new IllegalArgumentException("userId must not be null");
        }
        if (expiresAt == null) {
            throw new IllegalArgumentException("expiresAt must not be null");
        }
    }

    public static Session create(UUID userId, Duration ttl) {
        if (ttl == null || ttl.isZero() || ttl.isNegative()) {
            throw new IllegalArgumentException("ttl must be positive");
        }
        return new Session(UUID.randomUUID(), userId, Instant.now().plus(ttl));
    }

}
