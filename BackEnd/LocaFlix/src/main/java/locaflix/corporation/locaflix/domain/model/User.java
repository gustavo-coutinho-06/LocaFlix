package locaflix.corporation.locaflix.domain.model;

import java.util.UUID;

public record User(UUID id, String name, int age, String passwordHash, String email, Plan plan) {

    public User {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
        if (age < 0) {
            throw new IllegalArgumentException("age must be greater than or equal to zero");
        }
        if (passwordHash == null || passwordHash.isBlank()) {
            throw new IllegalArgumentException("passwordHash must not be blank");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("email must not be blank");
        }
        if (plan == null) {
            throw new IllegalArgumentException("plan must not be null");
        }
    }

    public static User create(String name, int age, String passwordHash, String email, Plan plan) {
        return new User(UUID.randomUUID(), name, age, passwordHash, email, plan);
    }

}
