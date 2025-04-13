package ru.otus.hw.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.models.Book;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BookServiceTest {

    @Autowired
    private BookService bookService;

    @PersistenceContext
    private EntityManager em;

    @Test
    void shouldSaveAndLoadBookCorrectly() {
        // Создаем и сохраняем книгу
        Book book = bookService.save("New Book", 1L, 1L);

        // Немедленно очищаем контекст, чтобы избежать влияния кэша уровня Persistence Context
        em.clear();

        // Проверяем, что книга была сохранена и её можно загрузить заново
        var savedBook = bookService.findById(book.getId()).orElseThrow();
        assertThat(savedBook).isNotNull();
        assertThat(savedBook.getTitle()).isEqualTo("New Book");

        // Проверяем, что связи (автор и жанр) загружены корректно
        assertThat(savedBook.getAuthor()).isNotNull();
        assertThat(savedBook.getAuthor().getFullName()).isEqualTo("Author_1"); // Из data.sql
        assertThat(savedBook.getGenre()).isNotNull();
        assertThat(savedBook.getGenre().getName()).isEqualTo("Genre_1"); // Из data.sql
    }

    @Test
    void shouldNotCauseLazyInitializationException() {
        // Получаем список всех книг
        var books = bookService.findAll();

        assertThat(books).isNotEmpty();

        // Проверяем, что доступ к связям внутри транзакции не вызывает LazyInitializationException
        for (Book book : books) {
            assertThat(book.getAuthor()).isNotNull();
            assertThat(book.getGenre()).isNotNull();
        }
    }
}