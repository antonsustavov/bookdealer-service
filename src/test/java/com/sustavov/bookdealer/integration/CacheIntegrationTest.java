package com.sustavov.bookdealer.integration;

import com.sustavov.bookdealer.cache.SpringCashName;
import com.sustavov.bookdealer.model.Author;
import com.sustavov.bookdealer.model.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Client API - Cache Integration Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CacheIntegrationTest extends BaseIntegrationTest {

    @Test
    @Order(1)
    void shouldGivenAuthorFromCacheAfterSave() {
        var firstName = "firstName";
        var lastName = "lastName";
        var testAuthor = createAuthorWithBooks(firstName, lastName);

        authorRepository.save(testAuthor);

        var authorById = ofNullable(authorService.getById(1L));

        assertEquals(authorById, getCachedAuthor(1L));

        authorService.delete(1L);

        assertEquals(Optional.empty(), getCachedAuthor(1L));
    }

    @Test
    @Order(2)
    void shouldGivenBookFromCacheAfterSave() {
        var firstName = "firstName";
        var lastName = "lastName";
        var testAuthor = createAuthorWithBooks(firstName, lastName);

        authorRepository.save(testAuthor);

        var bookById = ofNullable(bookService.getById(3L));

        assertEquals(bookById, getCachedBook(3L));

        bookService.delete(3L);

        assertEquals(Optional.empty(), getCachedBook(3L));
    }

    private Optional<Author> getCachedAuthor(Long id) {
        return ofNullable(cacheManager.getCache(SpringCashName.AUTHORS_ID)).map(c -> c.get(id, Author.class));
    }

    private Optional<Book> getCachedBook(Long id) {
        return ofNullable(cacheManager.getCache(SpringCashName.BOOKS_ID)).map(c -> c.get(id, Book.class));
    }

    private Author createAuthorWithBooks(String firstName, String lastName) {
        var the_lord_of_the_rings = createBook("The Lord of the Rings", "978-3-16-148710-0");
        var the_count_of_monte_cristo = createBook("The Count of Monte Cristo", "978-3-16-148410-0");
        var author = new Author();
        author.setFirstName(firstName);
        author.setLastName(lastName);
        author.setDateOfBirth(LocalDate.of(2021, 2, 2));
        author.setBooks(Set.of(the_count_of_monte_cristo, the_lord_of_the_rings));

        return author;
    }

    private Book createBook(String title, String isbn) {
        return Book.builder()
                .title(title)
                .isbn(isbn)
                .price(BigDecimal.valueOf(12.6))
                .publicationDate(LocalDate.of(1970, 2, 2))
                .build();
    }
}
