package locaflix.corporation.locaflix.application.rental;

import locaflix.corporation.locaflix.domain.exception.ConflictException;
import locaflix.corporation.locaflix.domain.exception.NotFoundException;
import locaflix.corporation.locaflix.domain.model.Movie;
import locaflix.corporation.locaflix.domain.model.Rental;
import locaflix.corporation.locaflix.domain.model.User;
import locaflix.corporation.locaflix.domain.repository.MovieRepository;
import locaflix.corporation.locaflix.domain.repository.RentalRepository;
import locaflix.corporation.locaflix.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class RentalService {

    private static final String ALREADY_RENTING = "User is already renting this movie";
    private static final String LIMIT_REACHED = "Rental limit reached for the current plan";

    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    public RentalService(
            RentalRepository rentalRepository,
            UserRepository userRepository,
            MovieRepository movieRepository
    ) {
        this.rentalRepository = rentalRepository;
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
    }

    @Transactional
    public Rental create(UUID userId, UUID movieId) {
        User user = findUser(userId);
        Movie movie = findMovie(movieId);
        if (rentalRepository.existsByUserIdAndMovieId(userId, movieId)) {
            throw new ConflictException(ALREADY_RENTING);
        }
        if (rentalRepository.countByUserId(userId) >= user.plan().allowedRentalQuantity()) {
            throw new ConflictException(LIMIT_REACHED);
        }
        return rentalRepository.save(Rental.create(user, movie));
    }

    public List<Rental> list(UUID userId) {
        return rentalRepository.findByUserId(userId);
    }

    public Rental get(UUID userId, UUID rentalId) {
        return getOwned(userId, rentalId);
    }

    @Transactional
    public Rental update(UUID userId, UUID rentalId, UUID movieId) {
        Rental current = getOwned(userId, rentalId);
        Movie movie = findMovie(movieId);
        boolean sameMovie = current.movie().id().equals(movie.id());
        if (!sameMovie && rentalRepository.existsByUserIdAndMovieId(userId, movie.id())) {
            throw new ConflictException(ALREADY_RENTING);
        }
        return rentalRepository.save(new Rental(current.id(), current.user(), movie, current.rentedAt()));
    }

    @Transactional
    public void delete(UUID userId, UUID rentalId) {
        getOwned(userId, rentalId);
        rentalRepository.deleteById(rentalId);
    }

    private Rental getOwned(UUID userId, UUID rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new NotFoundException("Rental not found"));
        if (!rental.user().id().equals(userId)) {
            throw new NotFoundException("Rental not found");
        }
        return rental;
    }

    private User findUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    private Movie findMovie(UUID movieId) {
        if (movieId == null) {
            throw new IllegalArgumentException("movieId must not be null");
        }
        return movieRepository.findById(movieId)
                .orElseThrow(() -> new NotFoundException("Movie not found"));
    }

}
