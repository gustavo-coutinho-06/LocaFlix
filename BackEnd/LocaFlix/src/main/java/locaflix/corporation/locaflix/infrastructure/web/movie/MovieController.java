package locaflix.corporation.locaflix.infrastructure.web.movie;

import locaflix.corporation.locaflix.application.movie.MovieService;
import locaflix.corporation.locaflix.domain.model.Movie;
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
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping
    public ResponseEntity<MovieResponse> create(@RequestBody MovieRequest request) {
        Movie movie = movieService.create(
                request.name(),
                request.imageUrl(),
                request.description(),
                request.rateStars(),
                request.year()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(MovieResponse.from(movie));
    }

    @GetMapping
    public List<MovieResponse> list() {
        return movieService.list().stream()
                .map(MovieResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public MovieResponse get(@PathVariable UUID id) {
        return MovieResponse.from(movieService.get(id));
    }

    @PutMapping("/{id}")
    public MovieResponse update(@PathVariable UUID id, @RequestBody MovieRequest request) {
        Movie movie = movieService.update(
                id,
                request.name(),
                request.imageUrl(),
                request.description(),
                request.rateStars(),
                request.year()
        );
        return MovieResponse.from(movie);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        movieService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
