package locaflix.corporation.locaflix.infrastructure.persistence.adapter;

import locaflix.corporation.locaflix.domain.exception.NotFoundException;
import locaflix.corporation.locaflix.domain.model.User;
import locaflix.corporation.locaflix.domain.repository.UserRepository;
import locaflix.corporation.locaflix.infrastructure.persistence.entity.PlanEntity;
import locaflix.corporation.locaflix.infrastructure.persistence.entity.UserEntity;
import locaflix.corporation.locaflix.infrastructure.persistence.jpa.PlanJpaRepository;
import locaflix.corporation.locaflix.infrastructure.persistence.jpa.UserJpaRepository;
import locaflix.corporation.locaflix.infrastructure.persistence.mapper.UserMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional(readOnly = true)
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final PlanJpaRepository planJpaRepository;

    public UserRepositoryAdapter(UserJpaRepository userJpaRepository, PlanJpaRepository planJpaRepository) {
        this.userJpaRepository = userJpaRepository;
        this.planJpaRepository = planJpaRepository;
    }

    @Override
    @Transactional
    public User save(User user) {
        PlanEntity planReference = planJpaRepository.getReferenceById(user.plan().id());
        UserEntity saved = userJpaRepository.save(UserMapper.toEntity(user, planReference));
        return UserMapper.toDomain(saved);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userJpaRepository.findById(id).map(UserMapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        return userJpaRepository.findAll().stream()
                .map(UserMapper::toDomain)
                .toList();
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        if (!userJpaRepository.existsById(id)) {
            throw new NotFoundException("User not found");
        }
        userJpaRepository.deleteById(id);
    }

}
