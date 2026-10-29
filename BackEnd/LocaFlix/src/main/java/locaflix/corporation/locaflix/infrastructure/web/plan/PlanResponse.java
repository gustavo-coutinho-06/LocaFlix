package locaflix.corporation.locaflix.infrastructure.web.plan;

import locaflix.corporation.locaflix.domain.model.Plan;

import java.util.UUID;

public record PlanResponse(UUID id, String name, int allowedRentalQuantity) {

    public static PlanResponse from(Plan plan) {
        return new PlanResponse(plan.id(), plan.name(), plan.allowedRentalQuantity());
    }

}
