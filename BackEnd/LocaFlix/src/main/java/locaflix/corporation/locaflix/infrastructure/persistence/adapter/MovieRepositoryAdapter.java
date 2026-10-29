package locaflix.corporation.locaflix.infrastructure.persistence.adapter;

import locaflix.corporation.locaflix.domain.exception.NotFoundException;
import locaflix.corporation.locaflix.domain.model.Movie;
import locaflix.corporation.locaflix.domain.repository.MovieRepository;
import locaflix.corporation.locaflix.infrastructure.persistence.entity.MovieEntity;
import locaflix.corporation.locaflix.infrastructure.persistence.jpa.MovieJpaRepository;
import locaflix.corporation.locaflix.infrastructure.persistence.mapper.MovieMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional(readOnly = true)
public class MovieRepositoryAdapter implements MovieRepository {

    private final MovieJpaRepository movieJpaRepository;

    public MovieRepositoryAdapter(MovieJpaRepository movieJpaRepository) {
        this.movieJpaRepository = movieJpaRepository;
    }

    @Override
    @Transactional
    public Movie save(Movie movie) {
        MovieEntity saved = movieJpaRepository.save(MovieMapper.toEntity(movie));
        return MovieMapper.toDomain(saved);
    }

    @Override
    public Optional<Movie> findById(UUID id) {
        return movieJpaRepository.findById(id).map(MovieMapper::toDomain);
    }

    @Override
    public List<Movie> findAll() {
        return movieJpaRepository.findAll().stream()
                .map(MovieMapper::toDomain)
                .toList();
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        if (!movieJpaRepository.existsById(id)) {
            throw new NotFoundException("Movie not found");
        }
        movieJpaRepository.deleteById(id);
    }

}
