package locaflix.corporation.locaflix.infrastructure.persistence.jpa;

import locaflix.corporation.locaflix.infrastructure.persistence.entity.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MovieJpaRepository extends JpaRepository<MovieEntity, UUID> {
}
