package ru.otus.hw.repositories;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({CommentRepositoryImpl.class, BookRepositoryImpl.class, AuthorRepositoryImpl.class, GenreRepositoryImpl.class})
@Transactional
class CommentRepositoryTest {

    static {
        System.setProperty("spring.profiles.active", "test");
    }

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

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
    void shouldFindCommentById() {

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

        Comment comment = new Comment();
        comment.setText("Test Comment");
        comment.setBook(book);
        comment = commentRepository.save(comment);

        Optional<Comment> foundComment = commentRepository.findById(comment.getId());
        assertThat(foundComment).isPresent();
        assertThat(foundComment.get()).isEqualTo(comment);
    }

    @Test
    void shouldSaveNewComment() {

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

        Comment comment = new Comment();
        comment.setText("New comment");
        comment.setBook(book);

        Comment savedComment = commentRepository.save(comment);
        assertThat(savedComment.getId()).isNotZero();
        assertThat(savedComment.getText()).isEqualTo("New comment");
    }
}