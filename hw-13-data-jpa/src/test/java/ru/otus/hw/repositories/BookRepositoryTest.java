package ru.otus.hw.repositories;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    static {
        System.setProperty("spring.profiles.active", "test");
    }

    @Autowired
    private JdbcTemplate jdbcTemplate; // For executing SQL scripts manually

    @BeforeEach
    void resetDatabase() {
        jdbcTemplate.execute("DELETE FROM comments");
        jdbcTemplate.execute("ALTER TABLE comments ALTER COLUMN id RESTART WITH 1");

        jdbcTemplate.execute("DELETE FROM books");
        jdbcTemplate.execute("ALTER TABLE books ALTER COLUMN id RESTART WITH 1");

        jdbcTemplate.execute("DELETE FROM authors");
        jdbcTemplate.execute("ALTER TABLE authors ALTER COLUMN id RESTART WITH 1");

        jdbcTemplate.execute("DELETE FROM genres");
        jdbcTemplate.execute("ALTER TABLE genres ALTER COLUMN id RESTART WITH 1");
    }

    @Test
    void shouldSaveAndFindBook() {

        Author author = new Author();
        author.setFullName("Test Author");
        author = authorRepository.save(author);

        Genre genre = new Genre();
        genre.setName("Test Genre");
        genre = genreRepository.save(genre);

        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthor(author);
        book.setGenre(genre);

        bookRepository.save(book);

        List<Book> books = bookRepository.findAll();

        assertThat(books).contains(book);
    }

    @Test
    void shouldDeleteBookById() {

        Author author = new Author();
        author.setFullName("Test Author");
        author = authorRepository.save(author);

        Genre genre = new Genre();
        genre.setName("Test Genre");
        genre = genreRepository.save(genre);

        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthor(author);
        book.setGenre(genre);
        book = bookRepository.save(book);

        bookRepository.deleteById(book.getId());

        Optional<Book> foundBook = bookRepository.findById(book.getId());

        assertThat(foundBook).isEmpty();
    }

}