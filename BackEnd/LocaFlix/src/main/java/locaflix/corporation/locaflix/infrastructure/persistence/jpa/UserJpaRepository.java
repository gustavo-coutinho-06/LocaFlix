package locaflix.corporation.locaflix.infrastructure.persistence.jpa;

import locaflix.corporation.locaflix.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {

    @Override
    @EntityGraph(attributePaths = "plan")
    Optional<UserEntity> findById(UUID id);

    @Override
    @EntityGraph(attributePaths = "plan")
    List<UserEntity> findAll();

}
