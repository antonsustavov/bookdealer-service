package com.sustavov.bookdealer.service;

import com.sustavov.bookdealer.exception.AuthorNotFoundException;
import com.sustavov.bookdealer.model.Author;
import com.sustavov.bookdealer.model.Book;
import com.sustavov.bookdealer.model.request.AuthorRequest;
import com.sustavov.bookdealer.repository.AuthorRepository;
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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {
    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private AuthorService authorService;

    @Test
    void create() {
        var authorRequestIn = createAuthorRequestIn();
        var authorRequestOut = createAuthorRequestOut();
        var authorIn = createAuthor(null, authorRequestIn);
        var authorOut = createAuthor(1L, authorRequestOut);

        when(authorRepository.save(authorIn)).thenReturn(authorOut);

        var actual = authorService.create(authorIn);

        assertThat(actual).isEqualTo(authorOut);
    }

    @Test
    void getAll() {
        when(authorRepository.findAll()).thenReturn(List.of());

        var actual = authorService.getAll();

        assertThat(actual).isEqualTo(List.of());
    }

    @Test
    void getById() {
        var authorRequest = createAuthorRequestOut();
        var author = Optional.of(createAuthor(1L, authorRequest));

        when(authorRepository.findById(1L)).thenReturn(author);

        var actual = authorService.getById(1L);

        assertThat(actual).isEqualTo(author.get());
    }

    @Test
    void getByIdThrowError() {
        when(authorRepository.findById(2L)).thenThrow(AuthorNotFoundException.class);

        assertThrows(AuthorNotFoundException.class, () -> {
            authorService.getById(2L);
        });
    }

    @Test
    void update() {
        var authorRequest = createAuthorRequestOut();
        var authorOut = createAuthor(1L, authorRequest);

        when(authorRepository.findById(1L)).thenReturn(Optional.of(authorOut));
        when(authorRepository.save(authorOut)).thenReturn(authorOut);

        var actual = authorService.update(1L, authorOut);

        assertThat(actual).isEqualTo(authorOut);
    }

    @Test
    void delete() {
        authorService.delete(1L);

        verify(authorRepository).deleteById(1L);
    }

    @Test
    void getByAuthor() {
        var authorRequest = createAuthorRequestOut();
        var authorOut = createAuthor(1L, authorRequest);

        when(authorRepository.findById(1L)).thenReturn(Optional.of(authorOut));
        when(bookRepository.findBooksByAuthor(authorOut)).thenReturn(List.of());

        var actual = authorService.getByAuthor(1L);

        assertThat(actual).isEqualTo(List.of());
    }

    @Test
    void sortedByFirstName() {
        List<Sort.Order> orders = List.of();
        var pagingSort = PageRequest.of(0, 2, Sort.by(orders));
        Page<Author> empty = Page.empty();

        when(authorRepository.findByFirstNameContaining("firstname", pagingSort)).thenReturn(empty);

        try (MockedStatic<SortedDirectionUtil> utilities = Mockito.mockStatic(SortedDirectionUtil.class)) {
            utilities.when(() -> SortedDirectionUtil.getOrders(new String[]{"id", "asc"})).thenReturn(List.of());
            var actual = authorService.sortedByFirstName("firstname", 0, 2, new String[]{"id", "asc"});
            assertThat(actual).isEqualTo(List.of());
        }
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