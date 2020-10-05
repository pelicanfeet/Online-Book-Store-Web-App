package com.mctiernan.mctiernanbooks;

public class BookTypeConversion {

	public Book convertToEntity(WebBook webBook) {
		Book book = new Book();
		book.setIsbn(webBook.getIsbn());
		book.setTitle(webBook.getTitle());
		book.setAuthor(webBook.getAuthor());
		book.setDescription(webBook.getDescription());
		book.setCost(webBook.getCost());
		book.setFormat(webBook.getFormat());
		book.setCategory(webBook.getCategory());
		return book;
	}
}