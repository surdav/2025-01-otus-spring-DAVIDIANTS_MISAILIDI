package ru.otus.hw.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.models.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class GenreServiceTest {

    @Autowired
    private GenreService genreService;

    @Test
    void shouldReturnAllGenres() {

        List<Genre> genres = genreService.findAll();

        assertThat(genres).isNotEmpty();
    }

    @Test
    void shouldReturnGenreById() {

        Genre genre = genreService.findById(1L).orElseThrow();

        assertThat(genre).isNotNull();

        assertThat(genre.getName()).isEqualTo("Genre_1");
    }
}