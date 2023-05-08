package com.sustavov.bookdealer.facade;

import com.sustavov.bookdealer.model.Author;
import com.sustavov.bookdealer.model.Book;
import com.sustavov.bookdealer.model.request.BookRequest;
import com.sustavov.bookdealer.model.response.BookResponse;
import com.sustavov.bookdealer.service.AuthorService;
import com.sustavov.bookdealer.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookFacade {
    private final BookService bookService;
    private final AuthorService authorService;

    public BookResponse create(BookRequest bookRequest) {
        var author = authorService.getReferenceById(bookRequest.getAuthorId());

        var newBook = toBook(bookRequest, author);

        var createdBook = bookService.create(newBook);

        return BookResponse.from(createdBook);
    }

    public List<BookResponse> getAll() {
        var all = bookService.getAll();

        return BookResponse.fromList(all);
    }

    public BookResponse getById(Long id) {
        var book = bookService.getById(id);

        return BookResponse.from(book);
    }

    public BookResponse update(Long id, BookRequest bookRequest) {
        var bookAuthor = authorService.getById(bookRequest.getAuthorId());

        var toBook = toBook(bookRequest, bookAuthor);

        var updateBook = bookService.update(id, toBook);

        return BookResponse.from(updateBook);
    }

    public void delete(Long id) {
        bookService.delete(id);
    }

    public List<Book> sortedByIsbn(String isbn, int page, int size, String[] sort) {
        return bookService.sortedByIsbn(isbn, page, size, sort);
    }

    private Book toBook(BookRequest bookRequest, Author author) {
        return Book.builder()
                .title(bookRequest.getTitle())
                .isbn(bookRequest.getIsbn())
                .publicationDate(bookRequest.getPublicationDate())
                .price(bookRequest.getPrice())
                .author(author)
                .build();
    }
}
