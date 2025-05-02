package ru.otus.hw.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.otus.hw.models.Book;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Sql(scripts = {"/clear.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@SpringBootTest
class BookServiceTest {

    @Autowired
    private BookService bookService;

    @PersistenceContext
    private EntityManager em;

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void shouldCreateAndLoadBookCorrectly() {

        Book book = bookService.create("New Book", 1L, 1L);

        // Immediately clearing the context to avoid the influence of the Persistence Context level cache
        em.clear();

        // Checking the book was saved and can be loaded again
        var savedBook = bookService.findById(book.getId()).orElseThrow();
        assertThat(savedBook).isNotNull();
        assertThat(savedBook.getTitle()).isEqualTo("New Book");

        // Checking the associations (author and genre) are loaded correctly
        assertThat(savedBook.getAuthor()).isNotNull();
        assertThat(savedBook.getAuthor().getFullName()).isEqualTo("Author_1"); // From data.sql
        assertThat(savedBook.getGenre()).isNotNull();
        assertThat(savedBook.getGenre().getName()).isEqualTo("Genre_1"); // From data.sql
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void shouldUpdateBookUsingDatabase() {
        var bookId = 1L;

        var updatedTitle = "Updated Book Title";

        Book updatedBook = bookService.update(bookId, updatedTitle);

        assertThat(updatedBook).isNotNull();

        assertThat(updatedBook.getTitle()).isEqualTo(updatedTitle);
    }

    /**
     * Ensures lazy-loaded relationships (e.g., author, genre) are accessible
     * without causing LazyInitializationException, as Hibernate requires an
     * active persistence context for initializing them.
     */
    @Test
    void shouldNotCauseLazyInitializationException() {

        var books = bookService.findAll();

        assertThat(books).isNotEmpty();

        // Checking that accessing relationships inside the transaction does not throw LazyInitializationException
        for (Book book : books) {
            assertThat(book.getAuthor()).isNotNull();
            assertThat(book.getGenre()).isNotNull();
        }
    }
}