package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class JdbcAuthorRepository implements AuthorRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<Author> findAll() {
        String sql = "SELECT id, full_name FROM authors";
        return jdbcTemplate.query(sql, new AuthorRowMapper());
    }

    @Override
    public Optional<Author> findById(long id) {
        String sql = "SELECT id, full_name FROM authors WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        List<Author> authors = jdbcTemplate.query(sql, params, new AuthorRowMapper());
        return authors.isEmpty() ? Optional.empty() : Optional.of(authors.get(0));
    }

    private static class AuthorRowMapper implements RowMapper<Author> {
        @Override
        public Author mapRow(ResultSet rs, int i) throws SQLException {
            Author author = new Author();
            author.setId(rs.getLong("id"));
            author.setFullName(rs.getString("full_name")); // Mapping full_name -> fullName
            return author;
        }
    }
}