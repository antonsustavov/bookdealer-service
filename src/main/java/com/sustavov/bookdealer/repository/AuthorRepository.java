package com.sustavov.bookdealer.repository;

import com.sustavov.bookdealer.model.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Page<Author> findByFirstNameContaining(String firstName, Pageable pageable);
}
