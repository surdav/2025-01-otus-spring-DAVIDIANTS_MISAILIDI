package ru.otus.hw.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.models.Comment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Test
    void shouldReturnCommentById() {
        // Проверяем, что метод возвращает комментарий по ID
        Comment comment = commentService.findById(1L).orElseThrow();

        assertThat(comment).isNotNull();
        assertThat(comment.getText()).isEqualTo("Great book!"); // значение из SQL
        assertThat(comment.getBook()).isNotNull(); // Проверяем доступ к связанным данным (книга)
    }

    @Test
    void shouldReturnAllCommentsForBook() {
        // Проверяем получение всех комментариев для конкретной книги
        List<Comment> comments = commentService.findByBookId(1L);

        assertThat(comments).isNotEmpty();
        for (Comment comment : comments) {
            assertThat(comment.getBook()).isNotNull(); // Проверяем связи, чтобы не было LazyInitializationException
        }
    }
}