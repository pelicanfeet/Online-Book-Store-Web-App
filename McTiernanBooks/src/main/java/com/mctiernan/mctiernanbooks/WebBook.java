package com.mctiernan.mctiernanbooks;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class WebBook {

	private String isbn;
	
	private String isbnConfirm;
	
	@NotNull
	@NotEmpty
	private String title;
	
	@Size(min=5, max=40)
	private String author;
	
	@Size(min=8, max=64, message="Description must be between 8 and 64 characters long")
	private String description;
	
	@Min(0)
	private double cost;
	
	@NotEmpty
	private List<String> format = new ArrayList<>();
	
	@NotEmpty(message="Category may not be empty")
	private String category;
	
	public WebBook() {}
	
	public WebBook(String isbn, String isbnConfirm, String title, String author,
				   double cost, ArrayList<String> format, String category) {
		this.isbn = isbn;
		this.isbnConfirm = isbnConfirm;
		this.title = title;
		this.author = author;
		this.cost = cost;
		this.format = format;
		this.category = category;
	}
	
	public String getIsbn() {
		return isbn;
	}
	
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getIsbnConfirm() {
		return isbnConfirm;
	}

	public void setIsbnConfirm(String isbnConfirm) {
		this.isbnConfirm = isbnConfirm;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public List<String> getFormat() {
		return format;
	}

	public void setFormat(List<String> format) {
		this.format = format;
	}

	public void addFormat(String format) {
		this.format.add(format);
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}