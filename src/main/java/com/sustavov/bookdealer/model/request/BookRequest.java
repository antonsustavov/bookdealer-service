package com.sustavov.bookdealer.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;

@Value
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookRequest {
    @NotEmpty(message = "The title is required.")
    String title;
    @NotEmpty(message = "The isbn is required.")
    String isbn;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @PastOrPresent(message = "The publication date must be present or in the past.")
    LocalDate publicationDate;
    @Positive(message = "The book price must be greater than 0")
    BigDecimal price;
    @Positive(message = "The book author is required.")
    Long authorId;
}
