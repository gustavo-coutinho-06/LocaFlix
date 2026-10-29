package locaflix.corporation.locaflix.application.plan;

import locaflix.corporation.locaflix.domain.exception.NotFoundException;
import locaflix.corporation.locaflix.domain.model.Plan;
import locaflix.corporation.locaflix.domain.repository.PlanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class PlanService {

    private final PlanRepository planRepository;

    public PlanService(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    @Transactional
    public Plan create(String name, int allowedRentalQuantity) {
        return planRepository.save(Plan.create(name, allowedRentalQuantity));
    }

    public List<Plan> list() {
        return planRepository.findAll();
    }

    public Plan get(UUID id) {
        return planRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Plan not found"));
    }

    @Transactional
    public Plan update(UUID id, String name, int allowedRentalQuantity) {
        get(id);
        return planRepository.save(new Plan(id, name, allowedRentalQuantity));
    }

    @Transactional
    public void delete(UUID id) {
        planRepository.deleteById(id);
    }

}
