package com.mctiernan.mctiernanbooks;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface BookDao {
	public Book createBook(Book book);
	public List<Book> getAllBooks();
	public Book getBookByIsbn(String isbn);
	public Book deleteBookByIsbn(String isbn);
	public List<String> getFormatTypes();
	public List<String> getCategoryTypes();
}