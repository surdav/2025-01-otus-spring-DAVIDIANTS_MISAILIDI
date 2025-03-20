package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository
public class JdbcBookRepository implements BookRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
public Optional<Book> findById(long id) {
    String sql = """
            SELECT 
                b.id AS book_id, 
                b.title AS book_title, 
                b.author_id AS author_id, 
                a.full_name AS author_name, 
                b.genre_id AS genre_id, 
                g.name AS genre_name
            FROM books b
            LEFT JOIN authors a ON b.author_id = a.id
            LEFT JOIN genres g ON b.genre_id = g.id
            WHERE b.id = :id
            """;

    MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("id", id);

    List<Book> books = jdbcTemplate.query(sql, params, new BookRowMapper());

    return books.stream().findFirst();
}

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            insert(book);
        } else {
            update(book);
        }
        return book;
    }

    @Override
    public void deleteById(long id) {

        String sql = "DELETE FROM books WHERE id = :id";

        jdbcTemplate.update(sql, new MapSqlParameterSource().addValue("id", id));
    }

    public void insert(Book book) {
        String sql = "INSERT INTO books (title, author_id, genre_id) " +
                "VALUES (:title, :authorId, :genreId)";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("title", book.getTitle())
                .addValue("authorId", book.getAuthor().getId())
                .addValue("genreId", book.getGenre().getId());

        KeyHolder keyHolder = new GeneratedKeyHolder(); // For storing the generated ID

        int rowsAffected = jdbcTemplate.update(sql, params, keyHolder, new String[]{"id"});

        // Retrieve the generated key and store it in a local variable
        Number key = keyHolder.getKey();

        // Check if rows were affected and the key is not null
        if (rowsAffected > 0 && key != null) {
            book.setId(key.longValue());
            log.info("Inserted book with ID: {}", book.getId());
        } else {
            log.info("Insert failed, no ID returned.");
            throw new IllegalStateException("Insert failed, no ID returned.");
        }
    }

    public Book update(Book book) {
        String sql = "UPDATE books " +
                "SET title = :title, author_id = :authorId, genre_id = :genreId " +
                "WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", book.getId())
                .addValue("title", book.getTitle())
                .addValue("authorId", book.getAuthor().getId())
                .addValue("genreId", book.getGenre().getId());

        int updatedRows = jdbcTemplate.update(sql, params);

        if (updatedRows == 0) {
            throw new EntityNotFoundException("Book with id=" + book.getId() + " not found.");
        }

        return book;
    }

    @Override
    public List<Book> findAll() {
        String sql = """
                SELECT 
                    b.id AS book_id, 
                    b.title AS book_title, 
                    b.author_id AS author_id, 
                    a.full_name AS author_name, 
                    b.genre_id AS genre_id, 
                    g.name AS genre_name
                FROM books b
                LEFT JOIN authors a ON b.author_id = a.id
                LEFT JOIN genres g ON b.genre_id = g.id
                """;

        // Using BookRowMapper that supports aliases
        return jdbcTemplate.query(sql, new BookRowMapper());
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            var book = new Book();

            // Map fields directly
            book.setId(rs.getLong("id"));
            book.setTitle(rs.getString("title"));

            // Map author
            var author = new Author();
            author.setId(rs.getLong("author_id"));
            author.setFullName(rs.getString("author_name"));
            book.setAuthor(author);

            // Map genre
            var genre = new Genre();
            genre.setId(rs.getLong("genre_id"));
            genre.setName(rs.getString("genre_name"));
            book.setGenre(genre);

            return book;
        }
    }
}
