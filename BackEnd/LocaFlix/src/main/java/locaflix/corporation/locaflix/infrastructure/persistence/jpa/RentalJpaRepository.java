package locaflix.corporation.locaflix.infrastructure.persistence.jpa;

import locaflix.corporation.locaflix.infrastructure.persistence.entity.RentalEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RentalJpaRepository extends JpaRepository<RentalEntity, UUID> {

    @Override
    @EntityGraph(attributePaths = {"user", "user.plan", "movie"})
    Optional<RentalEntity> findById(UUID id);

    @EntityGraph(attributePaths = {"user", "user.plan", "movie"})
    List<RentalEntity> findByUser_Id(UUID userId);

    boolean existsByUser_IdAndMovie_Id(UUID userId, UUID movieId);

    long countByUser_Id(UUID userId);

}
