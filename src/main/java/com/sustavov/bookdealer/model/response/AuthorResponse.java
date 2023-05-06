package com.sustavov.bookdealer.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sustavov.bookdealer.model.Author;
import com.sustavov.bookdealer.model.Book;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Value
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthorResponse {
    Long id;
    String firstName;
    String lastName;
    LocalDate dateOfBirth;
    @Builder.Default
    Set<Book> books = new HashSet<>();

    public static AuthorResponse from(Author author) {
        return AuthorResponse.builder()
                .id(author.getId())
                .firstName(author.getFirstName())
                .lastName(author.getLastName())
                .dateOfBirth(author.getDateOfBirth())
                .books(author.getBooks())
                .build();
    }

    public static List<AuthorResponse> fromList(List<Author> authors) {
        return authors.stream()
                .map(AuthorResponse::toAuthorResponse)
                .collect(Collectors.toList());
    }

    private static AuthorResponse toAuthorResponse(Author author) {
        return from(author);
    }
}
