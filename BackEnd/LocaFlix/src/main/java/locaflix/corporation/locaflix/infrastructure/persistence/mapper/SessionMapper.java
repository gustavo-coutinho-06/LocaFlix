package locaflix.corporation.locaflix.infrastructure.persistence.mapper;

import locaflix.corporation.locaflix.domain.model.Session;
import locaflix.corporation.locaflix.infrastructure.persistence.entity.SessionEntity;

public final class SessionMapper {

    private SessionMapper() {
    }

    public static Session toDomain(SessionEntity entity) {
        return new Session(entity.getId(), entity.getUserId(), entity.getExpiresAt());
    }

    public static SessionEntity toEntity(Session session) {
        return new SessionEntity(session.id(), session.userId(), session.expiresAt());
    }

}
