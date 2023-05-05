package com.sustavov.bookdealer.controller;

import com.sustavov.bookdealer.exception.ErrorMessage;
import com.sustavov.bookdealer.facade.BookFacade;
import com.sustavov.bookdealer.model.Book;
import com.sustavov.bookdealer.model.request.BookRequest;
import com.sustavov.bookdealer.model.response.BookResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Book Dealer Book API")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BookController {
    private final BookFacade bookFacade;

    @Operation(
            description = "Create Book",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successfully Created"),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)

                    )),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)

                    ))
            })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/books", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookResponse> create(@Valid @RequestBody BookRequest request) {
        BookResponse createdBook = bookFacade.create(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(createdBook.getId()).toUri();

        return ResponseEntity.created(location).body(createdBook);
    }

    @Operation(
            description = "Get All Books",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Get All Books"),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)

                    )),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)

                    ))
            }
    )
    @GetMapping("/books")
    public ResponseEntity<List<BookResponse>> getAll() {
        return ResponseEntity.ok(bookFacade.getAll());
    }

    @Operation(
            description = "Get Book By Id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Get Book By Id"),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)

                    )),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)

                    )),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)

                    ))
            }
    )
    @GetMapping("/books/{id}")
    public ResponseEntity<BookResponse> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(bookFacade.getById(id));
    }

    @Operation(
            description = "Update Book",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully Updated"),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)

                    )),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)

                    ))
            })
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(path = "/books/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookResponse> update(@PathVariable("id") Long id, @Valid @RequestBody BookRequest request) {
        BookResponse updatedBook = bookFacade.update(id, request);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/books/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookResponse> delete(@PathVariable("id") Long id) {
        bookFacade.delete(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/books/sorted/isbn")
    public ResponseEntity<List<Book>> sortedByIsbn(@RequestParam(required = false) String isbn,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "2") int size,
                                                   @RequestParam(defaultValue = "id,desc") String[] sort) {
        return ResponseEntity.ok(bookFacade.sortedByIsbn(isbn, page, size, sort));
    }

}
