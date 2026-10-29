package locaflix.corporation.locaflix.domain.model;

import java.util.UUID;

public record Plan(UUID id, String name, int allowedRentalQuantity) {

    public Plan {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
        if (allowedRentalQuantity <= 0) {
            throw new IllegalArgumentException("allowedRentalQuantity must be greater than zero");
        }
    }

    public static Plan create(String name, int allowedRentalQuantity) {
        return new Plan(UUID.randomUUID(), name, allowedRentalQuantity);
    }

}
