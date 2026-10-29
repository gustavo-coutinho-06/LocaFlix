package locaflix.corporation.locaflix.application.user;

import locaflix.corporation.locaflix.domain.exception.NotFoundException;
import locaflix.corporation.locaflix.domain.model.Plan;
import locaflix.corporation.locaflix.domain.model.User;
import locaflix.corporation.locaflix.domain.repository.PlanRepository;
import locaflix.corporation.locaflix.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PlanRepository planRepository;

    public UserService(UserRepository userRepository, PlanRepository planRepository) {
        this.userRepository = userRepository;
        this.planRepository = planRepository;
    }

    @Transactional
    public User create(String name, int age, String passwordHash, String email, UUID planId) {
        Plan plan = findPlan(planId);
        return userRepository.save(User.create(name, age, passwordHash, email, plan));
    }

    public List<User> list() {
        return userRepository.findAll();
    }

    public User get(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Transactional
    public User update(UUID id, String name, int age, String passwordHash, String email, UUID planId) {
        get(id);
        Plan plan = findPlan(planId);
        return userRepository.save(new User(id, name, age, passwordHash, email, plan));
    }

    @Transactional
    public void delete(UUID id) {
        userRepository.deleteById(id);
    }

    private Plan findPlan(UUID planId) {
        if (planId == null) {
            throw new IllegalArgumentException("planId must not be null");
        }
        return planRepository.findById(planId)
                .orElseThrow(() -> new NotFoundException("Plan not found"));
    }

}
