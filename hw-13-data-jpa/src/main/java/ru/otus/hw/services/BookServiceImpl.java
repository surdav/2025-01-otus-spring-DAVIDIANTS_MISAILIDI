package ru.otus.hw.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.BookRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final AuthorService authorService;

    private final GenreService genreService;

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Optional<Book> findById(long id) {
        return bookRepository.findById(id);
    }

    @Override
    @Transactional
    public Book create(String title, long authorId, long genreId) {

        var author = authorService
                .findById(authorId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Author not found: " + authorId)
                );

        var genre = genreService
                .findById(genreId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Genre not found: " + genreId)
                );

        var book = new Book(0L, title, author, genre, null);

        return bookRepository.save(book);
    }

    @Override
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Book update(long id, String title, long authorId, long genreId) {
        var existingBook = bookRepository
                .findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Book not found: " + id)
        );

        var author = authorService
                .findById(authorId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Author not found: " + authorId)
                );

        var genre = genreService
                .findById(genreId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Genre not found: " + genreId)
                );

        existingBook.setTitle(title);
        existingBook.setAuthor(author);
        existingBook.setGenre(genre);

        return bookRepository.save(existingBook);
    }

}