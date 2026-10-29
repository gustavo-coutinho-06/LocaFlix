package locaflix.corporation.locaflix.infrastructure.web.plan;

import locaflix.corporation.locaflix.application.plan.PlanService;
import locaflix.corporation.locaflix.domain.model.Plan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/plans")
public class PlanController {

    private final PlanService planService;

    public PlanController(PlanService planService) {
        this.planService = planService;
    }

    @PostMapping
    public ResponseEntity<PlanResponse> create(@RequestBody PlanRequest request) {
        Plan plan = planService.create(request.name(), request.allowedRentalQuantity());
        return ResponseEntity.status(HttpStatus.CREATED).body(PlanResponse.from(plan));
    }

    @GetMapping
    public List<PlanResponse> list() {
        return planService.list().stream()
                .map(PlanResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public PlanResponse get(@PathVariable UUID id) {
        return PlanResponse.from(planService.get(id));
    }

    @PutMapping("/{id}")
    public PlanResponse update(@PathVariable UUID id, @RequestBody PlanRequest request) {
        Plan plan = planService.update(id, request.name(), request.allowedRentalQuantity());
        return PlanResponse.from(plan);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        planService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
