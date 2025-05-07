package ru.otus.hw.services;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    public Optional<Comment> findById(long id) {
        return commentRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Comment> findByBookId(long bookId) {
        return commentRepository.findByBookId(bookId);
    }

    @Override
    @Transactional
    public Comment create(String text, Book book) {
        var comment = new Comment(text, book);
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public Comment update(long id, String text) {
        var comment = commentRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Comment not found: " + id)
        );

        comment.setText(text);

        return commentRepository.save(comment);
    }

    @Transactional
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }

    @Transactional
    @Deprecated // Marking the method as deprecated since ‘create’ and ‘update’ methods exist
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }
}