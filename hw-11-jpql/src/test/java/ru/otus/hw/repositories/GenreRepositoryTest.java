package ru.otus.hw.repositories;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(GenreRepositoryImpl.class)
@Transactional
class GenreRepositoryTest {

    static {
        System.setProperty("spring.profiles.active", "test");
    }

    @Autowired
    private GenreRepository genreRepository;

    @Test
    void shouldFindAllGenres() {
        List<Genre> genres = genreRepository.findAll();
        assertThat(genres)
                .isNotEmpty()
                .hasSizeGreaterThan(0);

    }

    @Test
    void shouldFindGenreById() {
        Optional<Genre> genre = genreRepository.findById(1L);
        assertThat(genre).isPresent();
        assertThat(genre.get().getName()).isEqualTo("Genre_1");
    }
}