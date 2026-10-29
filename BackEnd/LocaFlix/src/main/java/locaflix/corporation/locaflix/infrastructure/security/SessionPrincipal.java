package locaflix.corporation.locaflix.infrastructure.security;

import java.util.UUID;

public record SessionPrincipal(UUID userId, UUID sessionId) {
}
