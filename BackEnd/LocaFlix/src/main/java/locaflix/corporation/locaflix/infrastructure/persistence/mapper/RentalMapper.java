package locaflix.corporation.locaflix.infrastructure.persistence.mapper;

import locaflix.corporation.locaflix.domain.model.Rental;
import locaflix.corporation.locaflix.infrastructure.persistence.entity.MovieEntity;
import locaflix.corporation.locaflix.infrastructure.persistence.entity.RentalEntity;
import locaflix.corporation.locaflix.infrastructure.persistence.entity.UserEntity;

public final class RentalMapper {

    private RentalMapper() {
    }

    public static Rental toDomain(RentalEntity entity) {
        return new Rental(
                entity.getId(),
                UserMapper.toDomain(entity.getUser()),
                MovieMapper.toDomain(entity.getMovie()),
                entity.getRentedAt()
        );
    }

    public static RentalEntity toEntity(Rental rental, UserEntity user, MovieEntity movie) {
        return new RentalEntity(rental.id(), user, movie, rental.rentedAt());
    }

}
