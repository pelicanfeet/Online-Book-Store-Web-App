package com.mctiernan.mctiernanbooks;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BookController {

	@Autowired
	private WebBookValidator webBookValidator;

	private BookTypeConversion bookTypeConversion = new BookTypeConversion();

	@Autowired
	BookDao bookDao = new BookDaoImpl();
	
	@RequestMapping(value="/book/add", method=RequestMethod.GET)
	public String displayBookForm(WebBook webBook, Model model) {
		model.addAttribute("formatTypes", bookDao.getFormatTypes());
		model.addAttribute("categoryTypes", bookDao.getCategoryTypes());
		return "bookForm";
	}
	
	@RequestMapping(value="/book/add", method=RequestMethod.POST)
	public String processBook(@Valid WebBook webBook, BindingResult bindingResult, Model model) {
		webBookValidator.validate(webBook, bindingResult);
		if(bindingResult.hasErrors()) {
			model.addAttribute("formatTypes", bookDao.getFormatTypes());
			model.addAttribute("categoryTypes", bookDao.getCategoryTypes());
			return "bookForm";
		}
		Book book = bookTypeConversion.convertToEntity(webBook);
		model.addAttribute("book", book);
		bookDao.createBook(book);
		return "redirect:/book/detail?isbn=" + book.getIsbn();
	}

	@RequestMapping(value="/book/all", method=RequestMethod.GET)
	public String displayAllBooks(OrderItem orderItem, Model model) {
		model.addAttribute("books", bookDao.getAllBooks());
		return "books";
	}

	@RequestMapping(value="/book/detail", method=RequestMethod.GET)
	public String displayBookById(@RequestParam(value="isbn") String isbn, Model model) {
		model.addAttribute("isbn", isbn);
		model.addAttribute("book", bookDao.getBookByIsbn(isbn));
		if(bookDao.getBookByIsbn(isbn) == null)
			return "redirect:/book/all";
		return "book";
	}
	
	@RequestMapping(value="/book/delete", method=RequestMethod.GET)
	public String deleteBookByIsbn(@RequestParam(value="isbn") String isbn, Model model) {
		if(bookDao.getBookByIsbn(isbn) == null)
			return "books";
		bookDao.deleteBookByIsbn(isbn);
		return "redirect:/book/all";
	}
	
	@GetMapping("/book/searchForm")
	public String displaySearchForm(Model model) {
		model.addAttribute("book", new Book());
		return "searchForm";
	}
	
	@RequestMapping(value="/book/searchBook", method=RequestMethod.POST)
	public String getSearchBook(Book book) {
		String isbn = book.getIsbn();
		book = bookDao.getBookByIsbn(isbn);
		if(book != null)
			return "redirect:/book/detail?isbn=" + book.getIsbn();
		else
			return "redirect:/book/all";
	}
	
	@GetMapping("/book/deleteForm")
	public String displayDeleteForm(Model model) {
		model.addAttribute("book", new Book());
		return "deleteForm";
	}
	
	@RequestMapping(value="/book/deleteBook", method=RequestMethod.POST)
	public String deleteBook(Book book) {
		String isbn = book.getIsbn();
		book = bookDao.deleteBookByIsbn(isbn);
		return "redirect:/book/all";
	}
	
}