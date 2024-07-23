package com.efrei.endpoint;

import com.efrei.dto.Actor;
import com.efrei.model.Movie;
import com.efrei.model.MovieType;
import com.efrei.repository.MovieRepository;
import com.efrei.service.ActorService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieRepository movieRepository;
    private final ActorService actorService;

    @GetMapping
    public ResponseEntity<?> findAll() {
        var movies = movieRepository.findAll();

        if (movies.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            movies.forEach(this::populateMovieWithActors);

            return ResponseEntity.ok(movies);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findOne(@PathVariable Long id) {
        var movie = movieRepository.findById(id);

        return movie.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createMovie(@RequestBody Movie movie) {
        movieRepository.save(movie);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<?> updateMovie(@RequestBody Movie movie) {
        movieRepository.save(movie);

        return ResponseEntity.noContent().build();
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable Long id) {
        movieRepository.delete(movieRepository.findById(id).orElseThrow());

        return ResponseEntity.noContent().build();
    }

    private void populateMovieWithActors(Movie movie) {
        List<Actor> movieActors = actorService.getMovieActors(movie.getTitle());

        movieActors.forEach(movie::addActor);
    }

    @PostConstruct
    public void initDatabase() {
        var interstellar = Movie.builder()
                .director("Christopher Nolan")
                .releaseDate(LocalDate.of(2014, Month.NOVEMBER, 7))
                .type(MovieType.SCIFI)
                .title("Interstellar")
                .build();

        var snowden = Movie.builder()
                .director("Oliver Stone")
                .releaseDate(LocalDate.of(2016, Month.SEPTEMBER, 16))
                .type(MovieType.DRAMA)
                .title("Snowden")
                .build();

        var dune = Movie.builder()
            .director("Denis Villeneuve")
            .releaseDate(LocalDate.of(2021, Month.SEPTEMBER, 15))
            .type(MovieType.SCIFI)
            .title("Dune")
            .build();

        var lotr = Movie.builder()
            .director("Peter Jackson")
            .releaseDate(LocalDate.of(2003, Month.DECEMBER, 17))
            .type(MovieType.FANTAISY)
            .title("The Lord of the Rings: The Return of the King")
            .build();

        movieRepository.saveAll(List.of(interstellar, snowden, dune, lotr));
    }
}
