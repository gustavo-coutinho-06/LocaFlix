package locaflix.corporation.locaflix.domain.repository;

import locaflix.corporation.locaflix.domain.model.Plan;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlanRepository {

    Plan save(Plan plan);

    Optional<Plan> findById(UUID id);

    List<Plan> findAll();

}
