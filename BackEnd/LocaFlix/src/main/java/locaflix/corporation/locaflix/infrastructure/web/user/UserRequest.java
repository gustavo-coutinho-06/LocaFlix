package locaflix.corporation.locaflix.infrastructure.web.user;

import java.util.UUID;

public record UserRequest(String name, int age, String passwordHash, String email, UUID planId) {
}
