package ru.otus.hw.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.test.annotation.Rollback;

@SpringBootTest
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private BookService bookService;

    @PersistenceContext
    private EntityManager em;

    @BeforeEach
    @Transactional
    @Rollback
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
    @Transactional
    @Rollback
    void shouldDeleteCommentCorrectly() {

        commentService.deleteById(1L);

        em.flush(); // принудительно отправляем изменения в БД

        em.clear(); // очищаем контекст

        var comment = commentService.findById(1L);

        assertThat(comment).isEmpty();
    }

    @Test
    void testCheckInitialData() {
        var title = bookService.findById(1L).map(Book::getTitle).orElse("");

        assertThat(List.of("BookTitle_1", "Updated Book Title")).contains(title);
    }
}