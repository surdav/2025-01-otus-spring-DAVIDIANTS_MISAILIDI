package ru.otus.hw.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.otus.hw.models.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Sql(scripts = {"/clear.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
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