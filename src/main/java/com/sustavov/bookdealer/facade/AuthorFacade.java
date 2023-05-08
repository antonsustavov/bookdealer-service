package com.sustavov.bookdealer.facade;

import com.sustavov.bookdealer.model.Author;
import com.sustavov.bookdealer.model.request.AuthorRequest;
import com.sustavov.bookdealer.model.response.AuthorResponse;
import com.sustavov.bookdealer.model.response.BookResponse;
import com.sustavov.bookdealer.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthorFacade {
    private final AuthorService authorService;

    public AuthorResponse create(AuthorRequest authorRequest) {
        var newAuthor = toAuthor(authorRequest);

        var saved = authorService.create(newAuthor);

        return AuthorResponse.from(saved);
    }

    public List<AuthorResponse> getAll() {
        var allAuthors = authorService.getAll();

        return AuthorResponse.fromList(allAuthors);
    }

    public AuthorResponse getById(Long id) {
        var author = authorService.getById(id);

        return AuthorResponse.from(author);
    }

    public AuthorResponse update(Long id, AuthorRequest authorRequest) {
        var toAuthor = toAuthor(authorRequest);

        var updateAuthor = authorService.update(id, toAuthor);

        return AuthorResponse.from(updateAuthor);
    }

    public void delete(Long id) {
        authorService.delete(id);
    }

    public List<BookResponse> getByAuthor(Long id) {
        var books = authorService.getByAuthor(id);

        return BookResponse.fromList(books);
    }

    public List<Author> sortedByFirstName(String firstName, Integer page, Integer size, String[] sort) {
        return authorService.sortedByFirstName(firstName, page, size, sort);
    }

    private Author toAuthor(AuthorRequest authorRequest) {
        Author author = new Author();
        author.setFirstName(authorRequest.getFirstName());
        author.setLastName(authorRequest.getLastName());
        author.setDateOfBirth(authorRequest.getDateOfBirth());
        author.setBooks(authorRequest.getBooks());

        return author;
    }
}
