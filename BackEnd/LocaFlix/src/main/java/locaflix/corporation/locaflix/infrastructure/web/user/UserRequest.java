package locaflix.corporation.locaflix.infrastructure.web.user;

import java.util.UUID;

public record UserRequest(String name, int age, String password, String email, UUID planId) {
}
