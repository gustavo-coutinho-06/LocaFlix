package locaflix.corporation.locaflix.infrastructure.web.rental;

import locaflix.corporation.locaflix.domain.model.Rental;
import locaflix.corporation.locaflix.infrastructure.web.movie.MovieResponse;

import java.time.Instant;
import java.util.UUID;

public record RentalResponse(UUID id, UUID userId, MovieResponse movie, Instant rentedAt) {

    public static RentalResponse from(Rental rental) {
        return new RentalResponse(
                rental.id(),
                rental.user().id(),
                MovieResponse.from(rental.movie()),
                rental.rentedAt()
        );
    }

}
