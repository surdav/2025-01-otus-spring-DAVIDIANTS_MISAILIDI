package ru.otus.hw.services;

import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {

    List<Book> findAll();

    Optional<Book> findById(long id);

    Book create(String title, long authorId, long genreId);

    void deleteById(long id);

    Book update(long id, String title, long authorId, long genreId);
}