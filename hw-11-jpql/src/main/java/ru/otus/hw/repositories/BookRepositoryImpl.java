package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class BookRepositoryImpl implements BookRepository {

    @PersistenceContext
    private EntityManager em;

    public Optional<Book> findById(long id) {
        var graph = em.getEntityGraph("Book.authors-and-genres");
        return Optional.ofNullable(em.find(Book.class, id, Map.of(
                "jakarta.persistence.fetchgraph", graph
        )));
    }

    public List<Book> findAll() {
        var graph = em.getEntityGraph("Book.authors-and-genres");

        return em.createQuery("SELECT b FROM Book b", Book.class)
                .setHint("jakarta.persistence.fetchgraph", graph) // Указываем, что используем граф загрузки
                .getResultList();
    }

    public Book save(Book book) {
        if (book.getId() == 0) {
            em.persist(book);
            return book;
        } else {
            return em.merge(book);
        }
    }

    public void deleteById(long id) {
        findById(id).ifPresent(em::remove);
    }
}
