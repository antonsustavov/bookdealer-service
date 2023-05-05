package com.sustavov.bookdealer.controller;

import com.sustavov.bookdealer.exception.ErrorMessage;
import com.sustavov.bookdealer.facade.AuthorFacade;
import com.sustavov.bookdealer.model.Author;
import com.sustavov.bookdealer.model.request.AuthorRequest;
import com.sustavov.bookdealer.model.response.AuthorResponse;
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

@Tag(name = "Book Dealer Author API")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorFacade authorFacade;

    @Operation(
            description = "Create Book Author",
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
    @PostMapping(path = "/authors", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthorResponse> create(@Valid @RequestBody AuthorRequest request) {
        AuthorResponse createdAuthor = authorFacade.create(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(createdAuthor.getId()).toUri();

        return ResponseEntity.created(location).body(createdAuthor);
    }

    @GetMapping("/authors")
    public ResponseEntity<List<AuthorResponse>> getAll() {
        return ResponseEntity.ok(authorFacade.getAll());
    }

    @Operation(
            description = "Get Author By Id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Get Author By Id"),
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
    @GetMapping("/authors/{id}")
    public ResponseEntity<AuthorResponse> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(authorFacade.getById(id));
    }

    @Operation(
            description = "Update Book Author",
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
    @PutMapping(path = "/authors/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthorResponse> update(@PathVariable("id") Long id, @Valid @RequestBody AuthorRequest request) {
        AuthorResponse updatedAuthor = authorFacade.update(id, request);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/authors/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthorResponse> delete(@PathVariable("id") Long id) {
        authorFacade.delete(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/authors/{id}/books")
    public ResponseEntity<List<BookResponse>> getByAuthor(@PathVariable("id") Long id) {
        return ResponseEntity.ok(authorFacade.getByAuthor(id));
    }

    @GetMapping("/authors/sorted/firstname")
    public ResponseEntity<List<Author>> sortedByFirstName(@RequestParam(required = false) String firstName,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "2") int size,
                                          @RequestParam(defaultValue = "id,desc") String[] sort) {
        return ResponseEntity.ok(authorFacade.sortedByFirstName(firstName, page, size, sort));
    }

}
