package locaflix.corporation.locaflix.application.movie;

import locaflix.corporation.locaflix.domain.exception.NotFoundException;
import locaflix.corporation.locaflix.domain.model.Movie;
import locaflix.corporation.locaflix.domain.repository.MovieRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Transactional
    public Movie create(String name, String imageUrl, String description, int rateStars, int year) {
        return movieRepository.save(Movie.create(name, imageUrl, description, rateStars, year));
    }

    public List<Movie> list() {
        return movieRepository.findAll();
    }

    public Movie get(UUID id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Movie not found"));
    }

    @Transactional
    public Movie update(UUID id, String name, String imageUrl, String description, int rateStars, int year) {
        get(id);
        return movieRepository.save(new Movie(id, name, imageUrl, description, rateStars, year));
    }

    @Transactional
    public void delete(UUID id) {
        movieRepository.deleteById(id);
    }

}
