package com.sustavov.bookdealer.jpa;

import com.sustavov.bookdealer.model.Author;
import com.sustavov.bookdealer.model.Book;
import com.sustavov.bookdealer.repository.AuthorRepository;
import com.sustavov.bookdealer.repository.BookRepository;
import com.sustavov.bookdealer.util.SortedDirectionUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("Author - Data JPA Tests")
public class AuthorJpaTest {
    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    BookRepository bookRepository;

    @Test
    public void shouldGetEmptyAuthor() {
        List<Author> tutorials = authorRepository.findAll();

        assertThat(tutorials).isEmpty();
    }

    @Test
    public void shouldCreateAuthor() {
        var firstName = "firstName";
        var lastName = "lastName";
        Author testAuthor = createAuthorWithBooks(firstName, lastName);
        Author author = authorRepository.save(testAuthor);

        assertThat(author).hasFieldOrPropertyWithValue("firstName", "firstName");
        assertThat(author).hasFieldOrPropertyWithValue("lastName", "lastName");
        assertThat(author.getBooks().size()).isEqualTo(2);
    }

    @Test
    public void shouldUpdateAuthorWithBooks() {
        var firstName = "firstName";
        var lastName = "lastName";
        var newAuthor = createAuthorWithBooks(firstName, lastName);
        var savedAuthor = authorRepository.save(newAuthor);

        assertThat(savedAuthor).hasFieldOrPropertyWithValue("firstName", "firstName");
        assertThat(savedAuthor).hasFieldOrPropertyWithValue("lastName", "lastName");
        assertThat(savedAuthor.getBooks().size()).isEqualTo(2);


        savedAuthor.setFirstName("changedFirstName");
        var changedBooks = savedAuthor.getBooks().stream()
                .map(this::changeBook)
                .collect(Collectors.toSet());
        savedAuthor.setBooks(changedBooks);

        var updatedAuthor = authorRepository.save(savedAuthor);

        assertThat(updatedAuthor).hasFieldOrPropertyWithValue("firstName", "changedFirstName");
        assertThat(updatedAuthor).hasFieldOrPropertyWithValue("lastName", "lastName");
        assertThat(updatedAuthor.getBooks().size()).isEqualTo(2);

        assertThat(updatedAuthor.getBooks()).containsExactlyInAnyOrderElementsOf(changedBooks);
    }

    @Test
    public void shouldDeleteAuthor() {
        var firstName = "firstName";
        var lastName = "lastName";
        var testAuthor = createAuthorWithBooks(firstName, lastName);
        var savedAuthor = authorRepository.save(testAuthor);

        assertThat(savedAuthor).hasFieldOrPropertyWithValue("firstName", "firstName");
        assertThat(savedAuthor).hasFieldOrPropertyWithValue("lastName", "lastName");
        assertThat(savedAuthor.getBooks().size()).isEqualTo(2);

        authorRepository.deleteById(savedAuthor.getId());

        var findDeletedAuthorById = authorRepository.findById(savedAuthor.getId());
        var findDeletedAuthorBooks = bookRepository.findAll();

        assertThat(findDeletedAuthorById).isEmpty();
        assertThat(findDeletedAuthorBooks).isEmpty();
    }

    @Test
    public void shouldFindByFirstNameContaining() {
        var firstNameFirst = "firstName";
        var lastNameFirst = "lastName";
        var firstNameSecond = "Franz";
        var lastNameSecond = "Kafka";
        var testAuthorFirst = createAuthorWithBooks(firstNameFirst, lastNameFirst);
        var testAuthorSecond = createAuthorWithBooks(firstNameSecond, lastNameSecond);

        authorRepository.saveAll(Set.of(testAuthorFirst, testAuthorSecond));

        var allAuthors = authorRepository.findAll();

        assertThat(allAuthors.size()).isEqualTo(2);

        String[] sort = {"id", "desc"};
        List<Sort.Order> orders = SortedDirectionUtil.getOrders(sort);
        Pageable pagingSort = PageRequest.of(0, 2, Sort.by(orders));
        var actualAuthors = authorRepository.findByFirstNameContaining(firstNameSecond, pagingSort);

        assertThat(actualAuthors.getContent().size()).isEqualTo(1);
    }

    private Book changeBook(Book book) {
        book.setTitle("changedTitle");

        return book;
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

