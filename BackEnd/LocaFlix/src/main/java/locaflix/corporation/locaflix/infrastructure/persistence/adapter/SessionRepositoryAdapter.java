package locaflix.corporation.locaflix.infrastructure.persistence.adapter;

import locaflix.corporation.locaflix.domain.exception.NotFoundException;
import locaflix.corporation.locaflix.domain.model.Session;
import locaflix.corporation.locaflix.domain.repository.SessionRepository;
import locaflix.corporation.locaflix.infrastructure.persistence.entity.SessionEntity;
import locaflix.corporation.locaflix.infrastructure.persistence.jpa.SessionJpaRepository;
import locaflix.corporation.locaflix.infrastructure.persistence.mapper.SessionMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional(readOnly = true)
public class SessionRepositoryAdapter implements SessionRepository {

    private final SessionJpaRepository sessionJpaRepository;

    public SessionRepositoryAdapter(SessionJpaRepository sessionJpaRepository) {
        this.sessionJpaRepository = sessionJpaRepository;
    }

    @Override
    @Transactional
    public Session save(Session session) {
        SessionEntity saved = sessionJpaRepository.save(SessionMapper.toEntity(session));
        return SessionMapper.toDomain(saved);
    }

    @Override
    public Optional<Session> findById(UUID id) {
        return sessionJpaRepository.findById(id).map(SessionMapper::toDomain);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        if (!sessionJpaRepository.existsById(id)) {
            throw new NotFoundException("Session not found");
        }
        sessionJpaRepository.deleteById(id);
    }

}
