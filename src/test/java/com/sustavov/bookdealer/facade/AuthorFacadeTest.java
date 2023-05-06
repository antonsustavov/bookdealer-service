package com.sustavov.bookdealer.facade;

import com.sustavov.bookdealer.model.Author;
import com.sustavov.bookdealer.model.Book;
import com.sustavov.bookdealer.model.request.AuthorRequest;
import com.sustavov.bookdealer.model.response.AuthorResponse;
import com.sustavov.bookdealer.service.AuthorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorFacadeTest {
    @Mock
    private AuthorService authorService;
    @InjectMocks
    private AuthorFacade authorFacade;

    @Test
    void create() {
        var authorRequestIn = createAuthorRequestIn();
        var authorRequestOut = createAuthorRequestOut();

        var expectedAuthor = createAuthor(1L, authorRequestOut);
        var expected = createAuthorResponse(1L);

        when(authorService.create(any(Author.class))).thenReturn(expectedAuthor);

        var actual = authorFacade.create(authorRequestIn);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getAll() {
        when(authorService.getAll()).thenReturn(List.of());

        var actual = authorFacade.getAll();

        assertThat(actual).isEqualTo(List.of());
    }

    @Test
    void getById() {
        var authorRequestOut = createAuthorRequestOut();
        var author = createAuthor(1L, authorRequestOut);
        var expected = createAuthorResponse(1L);

        when(authorService.getById(1L)).thenReturn(author);

        var actual = authorFacade.getById(1L);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void update() {
        var authorRequestOut = createAuthorRequestOut();
        var author = createAuthor(1L, authorRequestOut);
        var expected = createAuthorResponse(1L);

        when(authorService.update(eq(1L), any(Author.class))).thenReturn(author);

        var actual = authorFacade.update(1L, authorRequestOut);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void delete() {
        authorFacade.delete(1L);

        verify(authorService).delete(1L);
    }

    @Test
    void getByAuthor() {
        when(authorService.getByAuthor(1L)).thenReturn(List.of());

        var actual = authorFacade.getByAuthor(1L);

        assertThat(actual).isEqualTo(List.of());
    }

    @Test
    void sortedByFirstName() {
        when(authorService.sortedByFirstName("firstname", 0, 2, new String[]{"id", "desc"})).thenReturn(List.of());

        var actual = authorFacade.sortedByFirstName("firstname", 0, 2, new String[]{"id", "desc"});

        assertThat(actual).isEqualTo(List.of());
    }

    private AuthorRequest createAuthorRequestIn() {
        return AuthorRequest.builder()
                .firstName("firstname")
                .lastName("lastname")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .books(new HashSet<>(Set.of(createBook(null), createBook(null))))
                .build();
    }

    private AuthorRequest createAuthorRequestOut() {
        return AuthorRequest.builder()
                .firstName("firstname")
                .lastName("lastname")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .books(new HashSet<>(Set.of(createBook(1L), createBook(2L))))
                .build();
    }

    private Book createBook(Long id) {
        return Book.builder()
                .id(id)
                .title("title")
                .isbn("isbn")
                .publicationDate(LocalDate.of(2000, 1, 1))
                .price(BigDecimal.ONE)
                .build();
    }

    private AuthorResponse createAuthorResponse(Long id) {
        return AuthorResponse.builder()
                .id(id)
                .firstName("firstname")
                .lastName("lastname")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .books(new HashSet<>(Set.of(createBook(1L), createBook(2L))))
                .build();
    }

    private Author createAuthor(Long id, AuthorRequest authorRequest) {
        Author author = new Author();
        author.setId(id);
        author.setFirstName(authorRequest.getFirstName());
        author.setLastName(authorRequest.getLastName());
        author.setDateOfBirth(authorRequest.getDateOfBirth());
        author.setBooks(authorRequest.getBooks());

        return author;
    }
}