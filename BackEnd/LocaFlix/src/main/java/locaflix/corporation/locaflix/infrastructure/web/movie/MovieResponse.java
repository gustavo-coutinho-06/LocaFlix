package locaflix.corporation.locaflix.infrastructure.web.movie;

import locaflix.corporation.locaflix.domain.model.Movie;

import java.util.UUID;

public record MovieResponse(UUID id, String name, String imageUrl, String description, int rateStars, int year) {

    public static MovieResponse from(Movie movie) {
        return new MovieResponse(
                movie.id(),
                movie.name(),
                movie.imageUrl(),
                movie.description(),
                movie.rateStars(),
                movie.year()
        );
    }

}
