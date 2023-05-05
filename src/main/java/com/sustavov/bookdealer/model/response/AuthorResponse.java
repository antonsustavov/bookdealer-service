package com.sustavov.bookdealer.model.response;

import com.sustavov.bookdealer.model.Author;
import com.sustavov.bookdealer.model.Book;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AuthorResponse {
    Long id;
    String firstName;
    String lastName;
    LocalDate dateOfBirth;
    Set<Book> books = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    public static AuthorResponse from(Author author) {
        AuthorResponse authorResponse = new AuthorResponse();
        authorResponse.setId(author.getId());
        authorResponse.setFirstName(author.getFirstName());
        authorResponse.setLastName(author.getLastName());
        authorResponse.setDateOfBirth(author.getDateOfBirth());
        authorResponse.setBooks(author.getBooks());

        return authorResponse;
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
