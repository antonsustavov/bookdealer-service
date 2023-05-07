package com.sustavov.bookdealer.service;

import com.sustavov.bookdealer.cache.SpringCashName;
import com.sustavov.bookdealer.exception.BookNotFoundException;
import com.sustavov.bookdealer.model.Book;
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
public class BookService {
    private final BookRepository bookRepository;

    @Transactional
    public Book create(Book book) {
        return bookRepository.save(book);
    }

    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    @Cacheable(value = SpringCashName.BOOKS_ID, key = "#id")
    public Book getById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found"));
    }

    @Transactional
    @CachePut(value = SpringCashName.BOOKS_ID, key = "#id")
    public Book update(Long id, Book book) {
        Book currentBook = getById(id);

        book.setId(currentBook.getId());

        return bookRepository.save(book);
    }

    @Transactional
    @CacheEvict(value = SpringCashName.BOOKS_ID, key = "#id")
    public void delete(Long id) {
        bookRepository.deleteById(id);
    }

    public List<Book> sortedByIsbn(String isbn, int page, int size, String[] sort) {
        List<Sort.Order> orders = getOrders(sort);

        Pageable pagingSort = PageRequest.of(page, size, Sort.by(orders));

        Page<Book> books;
        if (isbn == null) {
            books = bookRepository.findAll(pagingSort);
        } else {
            books = bookRepository.findByIsbnContaining(isbn, pagingSort);
        }

        return books.getContent();
    }

}
