package locaflix.corporation.locaflix.infrastructure.persistence.adapter;

import locaflix.corporation.locaflix.domain.exception.NotFoundException;
import locaflix.corporation.locaflix.domain.model.Plan;
import locaflix.corporation.locaflix.domain.repository.PlanRepository;
import locaflix.corporation.locaflix.infrastructure.persistence.entity.PlanEntity;
import locaflix.corporation.locaflix.infrastructure.persistence.jpa.PlanJpaRepository;
import locaflix.corporation.locaflix.infrastructure.persistence.mapper.PlanMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional(readOnly = true)
public class PlanRepositoryAdapter implements PlanRepository {

    private final PlanJpaRepository planJpaRepository;

    public PlanRepositoryAdapter(PlanJpaRepository planJpaRepository) {
        this.planJpaRepository = planJpaRepository;
    }

    @Override
    @Transactional
    public Plan save(Plan plan) {
        PlanEntity saved = planJpaRepository.save(PlanMapper.toEntity(plan));
        return PlanMapper.toDomain(saved);
    }

    @Override
    public Optional<Plan> findById(UUID id) {
        return planJpaRepository.findById(id).map(PlanMapper::toDomain);
    }

    @Override
    public List<Plan> findAll() {
        return planJpaRepository.findAll().stream()
                .map(PlanMapper::toDomain)
                .toList();
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        if (!planJpaRepository.existsById(id)) {
            throw new NotFoundException("Plan not found");
        }
        planJpaRepository.deleteById(id);
    }

}
