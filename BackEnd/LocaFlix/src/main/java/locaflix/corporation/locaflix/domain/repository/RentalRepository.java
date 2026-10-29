package locaflix.corporation.locaflix.domain.repository;

import locaflix.corporation.locaflix.domain.model.Rental;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RentalRepository {

    Rental save(Rental rental);

    Optional<Rental> findById(UUID id);

    List<Rental> findByUserId(UUID userId);

    boolean existsByUserIdAndMovieId(UUID userId, UUID movieId);

    int countByUserId(UUID userId);

    void deleteById(UUID id);

}
