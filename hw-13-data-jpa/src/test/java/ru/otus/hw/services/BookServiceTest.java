package ru.otus.hw.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Genre;

@SpringBootTest
@Transactional(propagation = Propagation.NEVER)
class BookServiceTest {

    @Autowired
    private BookService bookService;

    @PersistenceContext
    private EntityManager em;

    @Test
    void shouldCreateAndLoadBookCorrectly() {

        Book book = bookService.create("New Book", 1L, 1L);

        // Immediately clearing the context to avoid the influence of the Persistence Context level cache
        em.clear();

        // Checking the book was saved and can be loaded again
        var savedBook = bookService.findById(book.getId()).orElseThrow();
        assertThat(savedBook).isNotNull();

        // Creating the expected object
        var expectedAuthor = new Author(1L, "Author_1");
        var expectedGenre = new Genre(1L, "Genre_1");
        var expectedBook = new Book(
                savedBook.getId(),
                "New Book",
                expectedAuthor,
                expectedGenre,
                null);

        // Recursively comparing objects
        assertThat(savedBook)
                .usingRecursiveComparison()
                // Ignoring circular references
                .ignoringFields("author.books", "genre.books", "comments")
                .isEqualTo(expectedBook);
    }

    @Test
    void shouldUpdateBookUsingDatabase() {
        var bookId = 1L;

        var updatedTitle = "Updated Book Title";

        var updatedAuthorId = 2L;

        var updatedGenreId = 2L;

        Book updatedBook = bookService.update(bookId, updatedTitle, updatedAuthorId, updatedGenreId);

        assertThat(updatedBook).isNotNull();

        // Creating the expected object
        var expectedAuthor = new Author(updatedAuthorId, "Author_2");
        var expectedGenre = new Genre(updatedGenreId, "Genre_2");
        var expectedBook = new Book(
                bookId,
                updatedTitle,
                expectedAuthor,
                expectedGenre,
                null);

        // Recursively comparing objects
        assertThat(updatedBook)
                .usingRecursiveComparison()
                // Ignoring circular references
                .ignoringFields("author.books", "genre.books", "comments")
                .isEqualTo(expectedBook);
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