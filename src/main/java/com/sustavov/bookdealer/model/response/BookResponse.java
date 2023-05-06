package com.sustavov.bookdealer.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sustavov.bookdealer.model.Book;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookResponse {
    Long id;
    String title;
    String isbn;
    LocalDate publicationDate;
    BigDecimal price;
    Long authorId;

    public static BookResponse from(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .publicationDate(book.getPublicationDate())
                .price(book.getPrice())
                .authorId(book.getAuthor().getId())
                .build();
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
