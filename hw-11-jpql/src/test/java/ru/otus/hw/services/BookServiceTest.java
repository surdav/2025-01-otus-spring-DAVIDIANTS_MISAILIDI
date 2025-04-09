package ru.otus.hw.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.models.Book;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BookServiceTest {

    @Autowired
    private BookService bookService;

    @Test
    void shouldReturnAllBooksWithAuthorsAndGenres() {
        // Проверяем, что метод возвращает все книги вместе с авторами и жанрами
        List<Book> books = bookService.findAll();

        assertThat(books).isNotEmpty();

        // Проверка доступа к связанным сущностям (не вызывает LazyInitializationException)
        for (Book book : books) {
            assertThat(book.getAuthor()).isNotNull();
            assertThat(book.getGenre()).isNotNull();
        }
    }
}