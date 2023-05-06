package com.sustavov.bookdealer.service;

import com.sustavov.bookdealer.exception.AuthorNotFoundException;
import com.sustavov.bookdealer.model.Author;
import com.sustavov.bookdealer.model.Book;
import com.sustavov.bookdealer.repository.BookRepository;
import com.sustavov.bookdealer.util.SortedDirectionUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private BookService bookService;

    @Test
    void create() {
        var author = createAuthor(1L);
        var book = createBook(author);

        when(bookRepository.save(book)).thenReturn(book);

        var actual = bookService.create(book);

        assertThat(actual).isEqualTo(book);
    }

    @Test
    void getAll() {
        when(bookRepository.findAll()).thenReturn(List.of());

        var actual = bookService.getAll();

        assertThat(actual).isEqualTo(List.of());
    }

    @Test
    void getById() {
        var author = createAuthor(1L);
        var book = createBook(author);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        var actual = bookService.getById(1L);

        assertThat(actual).isEqualTo(book);
    }

    @Test
    void getByIdThrowError() {
        when(bookRepository.findById(2L)).thenThrow(AuthorNotFoundException.class);

        assertThrows(AuthorNotFoundException.class, () -> {
            bookService.getById(2L);
        });
    }

    @Test
    void update() {
        var author = createAuthor(1L);
        var book = createBook(author);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);

        var actual = bookService.update(1L, book);

        assertThat(actual).isEqualTo(book);
    }

    @Test
    void delete() {
        bookService.delete(1L);

        verify(bookRepository).deleteById(1L);
    }

    @Test
    void sortedByIsbn() {
        List<Sort.Order> orders = List.of();
        var pagingSort = PageRequest.of(0, 2, Sort.by(orders));
        Page<Book> empty = Page.empty();

        when(bookRepository.findByIsbnContaining("isbn", pagingSort)).thenReturn(empty);

        try (MockedStatic<SortedDirectionUtil> utilities = Mockito.mockStatic(SortedDirectionUtil.class)) {
            utilities.when(() -> SortedDirectionUtil.getOrders(new String[]{"id", "asc"})).thenReturn(List.of());
            var actual = bookService.sortedByIsbn("isbn", 0, 2, new String[]{"id", "asc"});
            assertThat(actual).isEqualTo(List.of());
        }
    }

    private Book createBook(Author author) {
        return Book.builder()
                .title("title")
                .isbn("isbn")
                .publicationDate(LocalDate.of(2000, 1, 1))
                .price(BigDecimal.TEN)
                .author(author)
                .build();
    }

    private Author createAuthor(Long id) {
        Author author = new Author();
        author.setId(id);
        author.setFirstName("firstname");
        author.setLastName("lastname");
        author.setDateOfBirth(LocalDate.of(2000, 1, 1));
        author.setBooks(Set.of(new Book()));

        return author;
    }
}