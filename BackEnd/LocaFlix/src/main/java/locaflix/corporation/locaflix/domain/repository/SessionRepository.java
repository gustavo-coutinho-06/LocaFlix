package locaflix.corporation.locaflix.domain.repository;

import locaflix.corporation.locaflix.domain.model.Session;

import java.util.Optional;
import java.util.UUID;

public interface SessionRepository {

    Session save(Session session);

    Optional<Session> findById(UUID id);

    void deleteById(UUID id);

}
