package locaflix.corporation.locaflix.domain.model;

import java.time.Instant;
import java.util.UUID;

public record Rental(UUID id, User user, Movie movie, Instant rentedAt) {

    public Rental {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        if (user == null) {
            throw new IllegalArgumentException("user must not be null");
        }
        if (movie == null) {
            throw new IllegalArgumentException("movie must not be null");
        }
        if (rentedAt == null) {
            throw new IllegalArgumentException("rentedAt must not be null");
        }
    }

    public static Rental create(User user, Movie movie) {
        return new Rental(UUID.randomUUID(), user, movie, Instant.now());
    }

}
