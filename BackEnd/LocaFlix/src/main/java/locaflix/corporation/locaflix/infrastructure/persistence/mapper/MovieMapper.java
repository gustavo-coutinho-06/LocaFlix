package locaflix.corporation.locaflix.infrastructure.persistence.mapper;

import locaflix.corporation.locaflix.domain.model.Movie;
import locaflix.corporation.locaflix.infrastructure.persistence.entity.MovieEntity;

public final class MovieMapper {

    private MovieMapper() {
    }

    public static Movie toDomain(MovieEntity entity) {
        return new Movie(
                entity.getId(),
                entity.getName(),
                entity.getImageUrl(),
                entity.getDescription(),
                entity.getRateStars(),
                entity.getYear()
        );
    }

    public static MovieEntity toEntity(Movie movie) {
        return new MovieEntity(
                movie.id(),
                movie.name(),
                movie.imageUrl(),
                movie.description(),
                movie.rateStars(),
                movie.year()
        );
    }

}
