package com.sustavov.bookdealer.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sustavov.bookdealer.model.Book;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Value
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthorRequest {
    @NotEmpty(message = "The full name is required.")
    @Size(min = 2, max = 100, message = "The length of full name must be between 2 and 100 characters.")
    String firstName;
    @NotEmpty(message = "The last name is required.")
    @Size(min = 2, max = 100, message = "The length of last name must be between 2 and 100 characters.")
    String lastName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Past(message = "The date of birth must be in the past.")
    LocalDate dateOfBirth;
    @Builder.Default
    Set<Book> books = new HashSet<>();
}
