package com.sustavov.bookdealer.facade;

import com.sustavov.bookdealer.model.Author;
import com.sustavov.bookdealer.model.Book;
import com.sustavov.bookdealer.model.request.BookRequest;
import com.sustavov.bookdealer.model.response.BookResponse;
import com.sustavov.bookdealer.service.AuthorService;
import com.sustavov.bookdealer.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookFacadeTest {
    @Mock
    private BookService bookService;
    @Mock
    private AuthorService authorService;
    @InjectMocks
    private BookFacade bookFacade;

    @Test
    void create() {
        var bookRequest = createBookRequest();
        var bookResponse = createBookResponse();
        var author = createAuthor(1L);
        var book = createBook(author);

        when(authorService.getReferenceById(1L)).thenReturn(author);
        when(bookService.create(any(Book.class))).thenReturn(book);

        var actual = bookFacade.create(bookRequest);

        assertThat(actual).isEqualTo(bookResponse);
    }

    @Test
    void getAll() {
        when(bookService.getAll()).thenReturn(List.of());

        var actual = bookFacade.getAll();

        assertThat(actual).isEqualTo(List.of());
    }

    @Test
    void getById() {
        var author = createAuthor(1L);
        var book = createBook(author);
        var expected = createBookResponse();

        when(bookService.getById(1L)).thenReturn(book);

        var actual = bookFacade.getById(1L);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void update() {
        var bookRequest = createBookRequest();
        var author = createAuthor(1L);
        var book = createBook(author);
        var expected = createBookResponse();

        when(authorService.getById(1L)).thenReturn(author);
        when(bookService.update(eq(1L), any(Book.class))).thenReturn(book);

        var actual = bookFacade.update(1L, bookRequest);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void delete() {
        bookFacade.delete(1L);

        verify(bookService).delete(1L);
    }

    @Test
    void sortedByIsbn() {
        when(bookService.sortedByIsbn("isbn", 0, 2, new String[]{"id", "desc"})).thenReturn(List.of());

        var actual = bookFacade.sortedByIsbn("isbn", 0, 2, new String[]{"id", "desc"});

        assertThat(actual).isEqualTo(List.of());
    }

    private BookResponse createBookResponse() {
        return BookResponse.builder()
                .title("title")
                .isbn("isbn")
                .publicationDate(LocalDate.of(2000, 1, 1))
                .price(BigDecimal.TEN)
                .authorId(1L)
                .build();
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

    private BookRequest createBookRequest() {
        return BookRequest.builder()
                .title("title")
                .isbn("isbn")
                .publicationDate(LocalDate.of(2000, 1, 1))
                .price(BigDecimal.TEN)
                .authorId(1L)
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