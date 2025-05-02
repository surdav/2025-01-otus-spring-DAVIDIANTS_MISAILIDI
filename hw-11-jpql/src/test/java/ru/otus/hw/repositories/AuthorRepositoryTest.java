package ru.otus.hw.repositories;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(AuthorRepositoryImpl.class)
@Transactional
class AuthorRepositoryTest {

    static {
        System.setProperty("spring.profiles.active", "test");
    }

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    void shouldFindAllAuthors() {
        List<Author> authors = authorRepository.findAll();
        assertThat(authors).isNotEmpty();
    }

    @Test
    void shouldFindAuthorById() {
        Optional<Author> author = authorRepository.findById(1L);
        assertThat(author).isPresent();
        assertThat(author.get().getFullName()).isEqualTo("Author_1");
    }

}