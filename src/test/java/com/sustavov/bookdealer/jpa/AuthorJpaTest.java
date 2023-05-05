package com.sustavov.bookdealer.jpa;

import com.sustavov.bookdealer.model.Author;
import com.sustavov.bookdealer.model.Book;
import com.sustavov.bookdealer.repository.AuthorRepository;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AuthorJpaTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    AuthorRepository repository;
    EasyRandom easyRandom = new EasyRandom();

    @Test
    public void shouldGetAkkEmptyAuthor() {
        List<Author> tutorials = repository.findAll();

        assertThat(tutorials).isEmpty();
    }

    @Test
    public void shouldCreateAuthor() {
        Author testAuthor = createAuthor();
        Author author = repository.save(testAuthor);

        assertThat(author).hasFieldOrPropertyWithValue("firstName", "firstName");
        assertThat(author).hasFieldOrPropertyWithValue("lastName", "lastName");
        assertThat(author.getBooks().size()).isEqualTo(2);
    }

    @Test
    public void shouldUpdateAuthor() {
        Author testAuthor = createAuthor();
        Author author = repository.save(testAuthor);

        assertThat(author).hasFieldOrPropertyWithValue("firstName", "firstName");
        assertThat(author).hasFieldOrPropertyWithValue("lastName", "lastName");
        assertThat(author.getBooks().size()).isEqualTo(2);

        author.setFirstName("newFirstName");
        Set<Book> collect = author.getBooks().stream().map(this::changeBook).collect(Collectors.toSet());
        author.setBooks(collect);

        Author updatedAuthor = repository.save(author);

        assertThat(updatedAuthor).hasFieldOrPropertyWithValue("firstName", "newFirstName");
        assertThat(updatedAuthor).hasFieldOrPropertyWithValue("lastName", "lastName");
        assertThat(updatedAuthor.getBooks().size()).isEqualTo(2);
    }

    @Test
    public void shouldDeleteAuthor() {
        Author testAuthor = createAuthor();
        Author author = repository.save(testAuthor);

        assertThat(author).hasFieldOrPropertyWithValue("firstName", "firstName");
        assertThat(author).hasFieldOrPropertyWithValue("lastName", "lastName");
        assertThat(author.getBooks().size()).isEqualTo(2);

        repository.deleteById(author.getId());

        Optional<Author> findById = repository.findById(author.getId());

        assertThat(findById).isEmpty();
    }

    private Book changeBook(Book book) {
        book.setTitle("newtitle");
        return book;
    }

    private Author createAuthor() {
        Book the_lord_of_the_rings = createBook("The Lord of the Rings", "978-3-16-148410-0");
        Book the_count_of_monte_cristo = createBook("The Count of Monte Cristo", "978-3-16-148410-0");
        Author author = new Author();
        author.setFirstName("firstName");
        author.setLastName("lastName");
        author.setDateOfBirth(LocalDate.of(2021, 2, 2));
        author.setBooks(Set.of(the_count_of_monte_cristo, the_lord_of_the_rings));

        return author;
    }

    private Book createBook(String title, String isbn) {
        Book book = new Book();
        book.setTitle(title);
        book.setIsbn(isbn);
        book.setPrice(BigDecimal.valueOf(12.6));
        book.setPublicationDate(LocalDate.of(1970, 2, 2));

        return book;
    }
}
