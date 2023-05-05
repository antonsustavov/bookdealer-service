package com.sustavov.bookdealer.jpa;

import com.sustavov.bookdealer.model.Author;
import com.sustavov.bookdealer.model.Book;
import com.sustavov.bookdealer.repository.AuthorRepository;
import com.sustavov.bookdealer.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BookJpaTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    BookRepository bookRepository;
    @Autowired
    AuthorRepository authorRepository;

    @Test
    public void shouldGetEmptyBook() {
        List<Book> books = bookRepository.findAll();

        assertThat(books).isEmpty();
    }

    @Test
    public void shouldCreateBook() {
        Author testAuthor = createAuthor();
        Author author = authorRepository.save(testAuthor);
        Book thirdBook = createBook("thirdBook", "978-3-16-148410-0");
        thirdBook.setAuthor(author);
        Book savedBook = bookRepository.save(thirdBook);

        List<Book> allBook = bookRepository.findAll();

        assertThat(allBook.size()).isEqualTo(3);
    }

    @Test
    public void shouldUpdateBook() {
        Author testAuthor = createAuthor();
        Author author = authorRepository.save(testAuthor);

        Optional<Book> byId = bookRepository.findById(1L);

        assertThat(byId).isNotEmpty();

        Book findBook = byId.get();
        findBook.setTitle("new title");

        Book actualBook = bookRepository.save(findBook);

        Optional<Author> actualAuthor = authorRepository.findById(author.getId());

        assertThat(actualAuthor).isNotEmpty();
        assertThat(actualAuthor.get().getBooks().size()).isEqualTo(2);
    }

    @Test
    public void shouldDeleteBook() {
        Author testAuthor = createAuthor();
        Author author = entityManager.persist(testAuthor);

        Book book = new Book();
        book.setTitle("ffff");
        book.setIsbn("ffff");
        book.setPrice(BigDecimal.valueOf(1));
        book.setAuthor(author);

        entityManager.persist(book);

        List<Book> beforeDelete = bookRepository.findAll();

        assertThat(beforeDelete.size()).isEqualTo(3);

        bookRepository.deleteById(book.getId());

        List<Book> afterDelete = bookRepository.findAll();

        assertThat(afterDelete.size()).isEqualTo(2);
    }

    @Test
    public void shouldFindBookByAuthor() {
        Author testAuthor = createAuthor();
        Author author = entityManager.persist(testAuthor);

        List<Book> booksByAuthor = bookRepository.findBooksByAuthor(author);

        assertThat(booksByAuthor.size()).isEqualTo(2);
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
