package com.sustavov.bookdealer.service;

import com.sustavov.bookdealer.cache.SpringCashName;
import com.sustavov.bookdealer.exception.AuthorNotFoundException;
import com.sustavov.bookdealer.model.Author;
import com.sustavov.bookdealer.model.Book;
import com.sustavov.bookdealer.repository.AuthorRepository;
import com.sustavov.bookdealer.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.sustavov.bookdealer.util.SortedDirectionUtil.getOrders;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @Transactional
    public Author create(Author author) {
        return authorRepository.save(author);
    }

    public List<Author> getAll() {
        return authorRepository.findAll();
    }

    @Cacheable(value = SpringCashName.AUTHORS_ID, key = "#id")
    public Author getById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException("Author not found"));
    }

    @Transactional
    @CachePut(value = SpringCashName.AUTHORS_ID, key = "#id")
    public Author update(Long id, Author author) {
        Author currentAuthor = getById(id);

        author.setId(currentAuthor.getId());

        return authorRepository.save(author);
    }

    @Transactional
    @CacheEvict(value = SpringCashName.AUTHORS_ID, key = "#id")
    public void delete(Long id) {
        authorRepository.deleteById(id);
    }

    public List<Book> getByAuthor(Long id) {
        Author author = getById(id);
        return bookRepository.findBooksByAuthor(author);
    }

    public List<Author> sortedByFirstName(String firstName, Integer page, Integer size, String[] sort) {
        List<Sort.Order> orders = getOrders(sort);

        Pageable pagingSort = PageRequest.of(page, size, Sort.by(orders));

        Page<Author> authors;
        if (firstName == null) {
            authors = authorRepository.findAll(pagingSort);
        } else {
            authors = authorRepository.findByFirstNameContaining(firstName, pagingSort);
        }

        return authors.getContent();
    }

    @Cacheable(value = SpringCashName.AUTHORS_ID, key = "#id")
    public Author getReferenceById(Long id) {
        return authorRepository.getReferenceById(id);
    }
}
