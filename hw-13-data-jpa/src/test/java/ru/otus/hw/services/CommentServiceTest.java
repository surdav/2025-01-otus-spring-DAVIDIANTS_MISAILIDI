package ru.otus.hw.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
    @Rollback
    void setupData() {
        commentService.save(
                new Comment("Integration Test Comment",
                        bookService.findById(1L).orElseThrow())
        );
    }

    @Test
    void shouldSaveAndLoadCommentCorrectly() {

        // Loading the book with ID = 1 to create a new comment
        var book = bookService.findById(1L).orElseThrow();

        // Creating and saving a new comment
        Comment newComment = new Comment("Fantastic book!", book);

        commentService.save(newComment);

        // Clearing the context
        em.clear();

        // Checking that the comment is saved
        List<Comment> comments = commentService.findByBookId(1L);

        assertThat(comments)
                .isNotEmpty()
                .anyMatch(comment -> comment.getText().equals("Fantastic book!"))
                .allSatisfy(comment -> assertThat(comment.getBook()).isNotNull());
    }

    @Test
    void shouldDeleteCommentCorrectly() {

        commentService.deleteById(1L);

        em.clear();

        var comment = commentService.findById(1L);

        assertThat(comment).isEmpty();
    }

    @Test
    void shouldCreateCommentCorrectly() {

        // Get a book for the comment
        Book book = bookService.findById(1L).orElseThrow();

        String commentText = "New Test Comment via create method";

        // Create the comment using the create method
        Comment createdComment = commentService.create(commentText, book);

        // Verify comment was created with correct data
        assertThat(createdComment).isNotNull();
        assertThat(createdComment.getId()).isGreaterThan(0);
        assertThat(createdComment.getText()).isEqualTo(commentText);

        // Store the ID for later use
        long commentId = createdComment.getId();

        // Clear persistence context to ensure we're loading from DB
        em.clear();

        // Retrieve the comment and verify its attributes
        Comment retrievedComment = commentService.findById(commentId).orElseThrow();

        // Compare essential fields individually instead of using recursive comparison
        assertThat(retrievedComment.getId()).isEqualTo(commentId);

        assertThat(retrievedComment.getText()).isEqualTo(commentText);

        assertThat(retrievedComment.getBook().getId()).isEqualTo(book.getId());
    }

    @Test
    void shouldUpdateCommentCorrectly() {

        // First, create a comment
        Book book = bookService.findById(1L).orElseThrow();

        Comment comment = commentService.create("Initial comment text", book);

        long commentId = comment.getId();

        // Clear persistence context
        em.clear();

        // Now update the comment
        String updatedText = "Updated comment text";
        Comment updatedComment = commentService.update(commentId, updatedText);

        // Verify the update worked correctly
        assertThat(updatedComment).isNotNull();
        assertThat(updatedComment.getId()).isEqualTo(commentId);
        assertThat(updatedComment.getText()).isEqualTo(updatedText);

        // Clear persistence context again
        em.clear();

        // Verify the updated comment is persisted in the database
        Comment retrievedComment = commentService.findById(commentId).orElseThrow();
        assertThat(retrievedComment.getText()).isEqualTo(updatedText);
    }
}