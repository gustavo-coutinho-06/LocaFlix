package locaflix.corporation.locaflix.infrastructure.web.user;

import locaflix.corporation.locaflix.domain.model.User;
import locaflix.corporation.locaflix.infrastructure.web.plan.PlanResponse;

import java.util.UUID;

public record UserResponse(UUID id, String name, int age, String email, PlanResponse plan) {

    public static UserResponse from(User user) {
        return new UserResponse(
                user.id(),
                user.name(),
                user.age(),
                user.email(),
                PlanResponse.from(user.plan())
        );
    }

}
