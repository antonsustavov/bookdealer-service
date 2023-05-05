package com.sustavov.bookdealer.model.response;

import com.sustavov.bookdealer.model.Book;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class BookResponse {
    Long id;
    String title;
    String isbn;
    LocalDate publicationDate;
    BigDecimal price;
    Long authorId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public static BookResponse from(Book book) {
        BookResponse bookResponse = new BookResponse();
        bookResponse.setId(book.getId());
        bookResponse.setIsbn(book.getIsbn());
        bookResponse.setTitle(book.getTitle());
        bookResponse.setPublicationDate(book.getPublicationDate());
        bookResponse.setPrice(book.getPrice());
        bookResponse.setAuthorId(book.getAuthor().getId());

        return bookResponse;
    }

    public static List<BookResponse> fromList(List<Book> books) {
        return books.stream()
                .map(BookResponse::toBookResponse)
                .collect(Collectors.toList());
    }

    private static BookResponse toBookResponse(Book book) {
        return from(book);
    }

}
