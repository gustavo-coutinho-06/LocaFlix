package locaflix.corporation.locaflix.domain.model;

import java.util.UUID;

public record Movie(UUID id, String name, String imageUrl, String description, int rateStars, int year) {

    public Movie {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
        if (imageUrl == null || imageUrl.isBlank()) {
            throw new IllegalArgumentException("imageUrl must not be blank");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("description must not be blank");
        }
        if (rateStars < 0 || rateStars > 5) {
            throw new IllegalArgumentException("rateStars must be between 0 and 5");
        }
        if (year <= 0) {
            throw new IllegalArgumentException("year must be greater than zero");
        }
    }

    public static Movie create(String name, String imageUrl, String description, int rateStars, int year) {
        return new Movie(UUID.randomUUID(), name, imageUrl, description, rateStars, year);
    }

}
