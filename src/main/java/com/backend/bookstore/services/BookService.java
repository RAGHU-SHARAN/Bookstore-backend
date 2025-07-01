package com.backend.bookstore.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.backend.bookstore.entities.Book;
import com.backend.bookstore.repositories.BookRepository;

@Service
public class BookService {

	private final BookRepository bookRepository;

	public BookService(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	public List<Book> getAllBooks() {
		return this.bookRepository.findAll();
	}

	public Book getBookById(Long Id) {
		return bookRepository.findById(Id).orElseThrow();
	}

	public Book createBook(Book book) {
		return bookRepository.save(book);
	}

	public void deleteBook(Long id) {
		bookRepository.deleteById(id);
	}
}
