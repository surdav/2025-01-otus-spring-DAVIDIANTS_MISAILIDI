package ru.otus.hw.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.models.Comment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.springframework.test.annotation.Rollback;

@SpringBootTest
@Transactional
@Rollback
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private BookService bookService;

    @PersistenceContext
    private EntityManager em;

    @BeforeEach
    void setupData() {
        commentService.save(
                new Comment("Integration Test Comment",
                        bookService.findById(1L).orElseThrow())
        );
    }

    @Test
    void shouldSaveAndLoadCommentCorrectly() {

        // Загружаем книгу с ID = 1 для создания нового комментария
        var book = bookService.findById(1L).orElseThrow();

        // Создаем и сохраняем новый комментарий
        Comment newComment = new Comment("Fantastic book!", book);

        commentService.save(newComment);

        // Очищаем контекст
        em.clear();

        // Проверяем, что комментарий сохранён
        List<Comment> comments = commentService.findByBookId(1L);

        assertThat(comments)
                .isNotEmpty()
                .anyMatch(comment -> comment.getText().equals("Fantastic book!"))
                .allSatisfy(comment -> assertThat(comment.getBook()).isNotNull());
    }

    @Test
    void shouldDeleteCommentCorrectly() {
        // Загрузка книги и её комментариев
        var book = bookService.findById(1L).orElseThrow();

        var comment = commentService.findById(1L).orElseThrow();

        // Удаление комментария из коллекции
        book.getComments().removeIf(c -> c.getId() == comment.getId());

        // Обновляем книгу, чтобы сработал orphanRemoval
        em.flush();

        em.clear();

        // Проверка, что комментарий удалён
        var deletedComment = commentService.findById(1L);

        assertThat(deletedComment).isEmpty();
    }

    @Test
    void testCheckInitialData() {
        assertTrue(bookService.findById(1L).isPresent(), "Book with id 1 must exist");

        assertEquals("BookTitle_1", bookService.findById(1L).get().getTitle());
    }
}