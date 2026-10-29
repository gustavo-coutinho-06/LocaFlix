package locaflix.corporation.locaflix.infrastructure.persistence.jpa;

import locaflix.corporation.locaflix.infrastructure.persistence.entity.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SessionJpaRepository extends JpaRepository<SessionEntity, UUID> {
}
