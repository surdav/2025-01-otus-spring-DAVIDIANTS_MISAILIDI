package ru.otus.hw.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.models.Author;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AuthorServiceTest {

    @Autowired
    private AuthorService authorService;

    @Test
    void shouldReturnAllAuthors() {

        List<Author> authors = authorService.findAll();

        assertThat(authors).isNotEmpty();
    }

    @Test
    void shouldReturnAuthorById() {

        Author author = authorService.findById(1L).orElseThrow();

        assertThat(author).isNotNull();
        assertThat(author.getFullName()).isEqualTo("Author_1");
    }
}