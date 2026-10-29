package locaflix.corporation.locaflix.infrastructure.persistence.adapter;

import locaflix.corporation.locaflix.domain.exception.NotFoundException;
import locaflix.corporation.locaflix.domain.model.Rental;
import locaflix.corporation.locaflix.domain.repository.RentalRepository;
import locaflix.corporation.locaflix.infrastructure.persistence.entity.MovieEntity;
import locaflix.corporation.locaflix.infrastructure.persistence.entity.RentalEntity;
import locaflix.corporation.locaflix.infrastructure.persistence.entity.UserEntity;
import locaflix.corporation.locaflix.infrastructure.persistence.jpa.MovieJpaRepository;
import locaflix.corporation.locaflix.infrastructure.persistence.jpa.RentalJpaRepository;
import locaflix.corporation.locaflix.infrastructure.persistence.jpa.UserJpaRepository;
import locaflix.corporation.locaflix.infrastructure.persistence.mapper.RentalMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional(readOnly = true)
public class RentalRepositoryAdapter implements RentalRepository {

    private final RentalJpaRepository rentalJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final MovieJpaRepository movieJpaRepository;

    public RentalRepositoryAdapter(
            RentalJpaRepository rentalJpaRepository,
            UserJpaRepository userJpaRepository,
            MovieJpaRepository movieJpaRepository
    ) {
        this.rentalJpaRepository = rentalJpaRepository;
        this.userJpaRepository = userJpaRepository;
        this.movieJpaRepository = movieJpaRepository;
    }

    @Override
    @Transactional
    public Rental save(Rental rental) {
        UserEntity userReference = userJpaRepository.getReferenceById(rental.user().id());
        MovieEntity movieReference = movieJpaRepository.getReferenceById(rental.movie().id());
        RentalEntity saved = rentalJpaRepository.save(RentalMapper.toEntity(rental, userReference, movieReference));
        return RentalMapper.toDomain(saved);
    }

    @Override
    public Optional<Rental> findById(UUID id) {
        return rentalJpaRepository.findById(id).map(RentalMapper::toDomain);
    }

    @Override
    public List<Rental> findByUserId(UUID userId) {
        return rentalJpaRepository.findByUser_Id(userId).stream()
                .map(RentalMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByUserIdAndMovieId(UUID userId, UUID movieId) {
        return rentalJpaRepository.existsByUser_IdAndMovie_Id(userId, movieId);
    }

    @Override
    public int countByUserId(UUID userId) {
        return Math.toIntExact(rentalJpaRepository.countByUser_Id(userId));
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        if (!rentalJpaRepository.existsById(id)) {
            throw new NotFoundException("Rental not found");
        }
        rentalJpaRepository.deleteById(id);
    }

}
