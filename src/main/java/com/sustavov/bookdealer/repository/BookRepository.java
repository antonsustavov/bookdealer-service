package com.sustavov.bookdealer.repository;

import com.sustavov.bookdealer.model.Author;
import com.sustavov.bookdealer.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findBooksByAuthor(Author author);
    Page<Book> findByIsbnContaining(String isbn, Pageable pageable);
}
