package locaflix.corporation.locaflix.infrastructure.persistence.mapper;

import locaflix.corporation.locaflix.domain.model.User;
import locaflix.corporation.locaflix.infrastructure.persistence.entity.PlanEntity;
import locaflix.corporation.locaflix.infrastructure.persistence.entity.UserEntity;

public final class UserMapper {

    private UserMapper() {
    }

    public static User toDomain(UserEntity entity) {
        return new User(
                entity.getId(),
                entity.getName(),
                entity.getAge(),
                entity.getPasswordHash(),
                entity.getEmail(),
                PlanMapper.toDomain(entity.getPlan())
        );
    }

    public static UserEntity toEntity(User user, PlanEntity plan) {
        return new UserEntity(
                user.id(),
                user.name(),
                user.age(),
                user.passwordHash(),
                user.email(),
                plan
        );
    }

}
