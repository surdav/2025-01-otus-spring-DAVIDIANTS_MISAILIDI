package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

@Repository
public class CommentRepositoryImpl implements CommentRepository {

    @PersistenceContext
    private EntityManager em;

    public Optional<Comment> findById(long id) {
        return Optional.ofNullable(em.find(Comment.class, id));
    }

    public List<Comment> findByBookId(long bookId) {
        return em.createQuery(
                "SELECT c FROM Comment c WHERE c.book.id = :bookId",
                        Comment.class)
                .setParameter("bookId", bookId)
                .getResultList();
    }

    public Comment save(Comment comment) {
        if (comment.getId() == 0) {
            em.persist(comment);
            return comment;
        } else {
            return em.merge(comment);
        }
    }

    public void deleteById(long id) {
        findCommentOptionalById(id).ifPresent(em::remove);
    }

    private Optional<Comment> findCommentOptionalById(long id) {
        return Optional.ofNullable(em.find(Comment.class, id));
    }
}
