package com.mctiernan.mctiernanbooks;

import java.util.ArrayList;
import java.util.List;

public class Book {

	private String isbn;
	private String title;
	private String author;
	private String description;
	private double cost;
	private List<String> format = new ArrayList<>();
	private String category;

	public Book() {}

	public Book(String isbn, String title, String author, String description,
			    double cost, ArrayList<String> format, String category) {
		this.isbn = isbn;
		this.title = title;
		this.author = author;
		this.description = description;
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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}