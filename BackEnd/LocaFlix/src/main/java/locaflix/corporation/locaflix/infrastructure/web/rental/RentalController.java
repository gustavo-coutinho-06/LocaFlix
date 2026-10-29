package locaflix.corporation.locaflix.infrastructure.web.rental;

import locaflix.corporation.locaflix.application.rental.RentalService;
import locaflix.corporation.locaflix.domain.model.Rental;
import locaflix.corporation.locaflix.infrastructure.security.CurrentSession;
import locaflix.corporation.locaflix.infrastructure.security.SessionPrincipal;
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
@RequestMapping("/rentals")
public class RentalController {

    private final RentalService rentalService;
    private final CurrentSession currentSession;

    public RentalController(RentalService rentalService, CurrentSession currentSession) {
        this.rentalService = rentalService;
        this.currentSession = currentSession;
    }

    @PostMapping
    public ResponseEntity<RentalResponse> create(@RequestBody RentalRequest request) {
        Rental rental = rentalService.create(currentUserId(), request.movieId());
        return ResponseEntity.status(HttpStatus.CREATED).body(RentalResponse.from(rental));
    }

    @GetMapping
    public List<RentalResponse> list() {
        return rentalService.list(currentUserId()).stream()
                .map(RentalResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public RentalResponse get(@PathVariable UUID id) {
        return RentalResponse.from(rentalService.get(currentUserId(), id));
    }

    @PutMapping("/{id}")
    public RentalResponse update(@PathVariable UUID id, @RequestBody RentalRequest request) {
        Rental rental = rentalService.update(currentUserId(), id, request.movieId());
        return RentalResponse.from(rental);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        rentalService.delete(currentUserId(), id);
        return ResponseEntity.noContent().build();
    }

    private UUID currentUserId() {
        SessionPrincipal principal = currentSession.require();
        return principal.userId();
    }

}
