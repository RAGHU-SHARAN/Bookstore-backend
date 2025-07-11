package com.backend.bookstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.bookstore.entities.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

}
