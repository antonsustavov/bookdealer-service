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

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("Author - Data JPA Tests")
public class BookJpaTest {
    @Autowired
    BookRepository bookRepository;
    @Autowired
    AuthorRepository authorRepository;

    @Test
    public void shouldGetEmptyBook() {
        var books = bookRepository.findAll();

        assertThat(books).isEmpty();
    }

    @Test
    public void shouldCreateBook() {
        var testAuthor = createAuthor();
        var author = authorRepository.save(testAuthor);
        var thirdBook = createBook("thirdBook", "978-3-16-148410-0");
        thirdBook.setAuthor(author);

        bookRepository.save(thirdBook);

        var allBook = bookRepository.findAll();

        assertThat(allBook.size()).isEqualTo(3);
    }

    @Test
    public void shouldUpdateBook() {
        var testAuthor = createAuthor();
        var author = authorRepository.save(testAuthor);

        var findBookById = bookRepository.findById(1L);

        assertThat(findBookById).isNotEmpty();

        var findBook = findBookById.get();
        findBook.setTitle("new title");

        bookRepository.save(findBook);

        var actualAuthor = authorRepository.findById(author.getId());

        assertThat(actualAuthor).isNotEmpty();
        assertThat(actualAuthor.get().getBooks().size()).isEqualTo(2);
    }

    @Test
    public void shouldDeleteBook() {
        var testAuthor = createAuthor();
        var author = authorRepository.save(testAuthor);

        var book = createBook("title", "isbn");
        book.setAuthor(author);

        bookRepository.save(book);

        var beforeDelete = bookRepository.findAll();

        assertThat(beforeDelete.size()).isEqualTo(3);

        bookRepository.deleteById(book.getId());

        var afterDelete = bookRepository.findAll();

        assertThat(afterDelete.size()).isEqualTo(2);
    }

    @Test
    public void shouldFindBookByAuthor() {
        var testAuthor = createAuthor();
        var author = authorRepository.save(testAuthor);

        var booksByAuthor = bookRepository.findBooksByAuthor(author);

        assertThat(booksByAuthor.size()).isEqualTo(2);
    }

    @Test
    public void shouldFindByFirstNameContaining() {
        var testAuthor = createAuthor();
        var savedAuthor = authorRepository.save(testAuthor);

        var isbn = "888-987-567-890-1";
        var book = createBook("title", isbn);
        book.setAuthor(savedAuthor);

        bookRepository.save(book);

        String[] sort = {"id", "desc"};
        List<Sort.Order> orders = SortedDirectionUtil.getOrders(sort);
        Pageable pagingSort = PageRequest.of(0, 2, Sort.by(orders));
        var actualBooks = bookRepository.findByIsbnContaining(isbn, pagingSort);

        assertThat(actualBooks.getContent().size()).isEqualTo(1);
    }

    private Author createAuthor() {
        Book the_lord_of_the_rings = createBook("The Lord of the Rings", "978-3-16-148410-0");
        Book the_count_of_monte_cristo = createBook("The Count of Monte Cristo", "978-3-16-148410-0");
        var author = new Author();
        author.setFirstName("firstName");
        author.setLastName("lastName");
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
