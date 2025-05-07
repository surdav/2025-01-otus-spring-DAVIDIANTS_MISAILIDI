package ru.otus.hw.services;

import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    Optional<Comment> findById(long id);

    List<Comment> findByBookId(long bookId);

    Comment create(String text, Book book);

    Comment update(long id, String text);

    void deleteById(long id);

    Comment save(Comment comment);
}
