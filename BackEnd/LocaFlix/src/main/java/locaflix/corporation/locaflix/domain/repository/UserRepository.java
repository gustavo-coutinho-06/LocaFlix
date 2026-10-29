package locaflix.corporation.locaflix.domain.repository;

import locaflix.corporation.locaflix.domain.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(UUID id);

    List<User> findAll();

}
