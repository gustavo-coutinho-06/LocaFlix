package locaflix.corporation.locaflix.infrastructure.persistence.mapper;

import locaflix.corporation.locaflix.domain.model.Plan;
import locaflix.corporation.locaflix.infrastructure.persistence.entity.PlanEntity;

public final class PlanMapper {

    private PlanMapper() {
    }

    public static Plan toDomain(PlanEntity entity) {
        return new Plan(entity.getId(), entity.getName(), entity.getAllowedRentalQuantity());
    }

    public static PlanEntity toEntity(Plan plan) {
        return new PlanEntity(plan.id(), plan.name(), plan.allowedRentalQuantity());
    }

}
