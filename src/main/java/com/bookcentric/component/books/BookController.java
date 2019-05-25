package com.bookcentric.component.books;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.bookcentric.component.books.author.Author;
import com.bookcentric.component.books.author.AuthorService;
import com.bookcentric.component.books.genre.Genre;
import com.bookcentric.component.books.genre.GenreService;
import com.bookcentric.component.books.publisher.Publisher;
import com.bookcentric.component.books.publisher.PublisherService;

@Controller
public class BookController {	
	
	@Autowired BookService bookService;
	@Autowired AuthorService authorService;
	@Autowired PublisherService publisherService;
	@Autowired GenreService genreService;
	
	@GetMapping("/book/entry")
	public ModelAndView viewBookEntry() {
		ModelAndView bookView = new ModelAndView("book-entry");
		
		Books book = new Books();
		List<Author> authorList = authorService.getAll();
		List<Publisher> publisherList = publisherService.getAll();
		List<Genre> genreList = genreService.getAll();
		
		bookView.addObject("pageTitle", "BookCentric - Book entry");
		bookView.addObject("book", book);
		bookView.addObject("authorList", authorList);
		bookView.addObject("publisherList", publisherList);
		bookView.addObject("genreList", genreList);
		
		return bookView;
	} 
	
	@PostMapping("/book/add")
	public String addBooks(Books book) {
		bookService.add(book);
		
		return "redirect:/book/entry";
	}
	
	@GetMapping("/book/inventory")
	public ModelAndView getbooks() {
		ModelAndView bookView = new ModelAndView("book-inventory");
		
		List<Books> books = bookService.getAll();
		List<BooksDTO> bookList = bookService.getFilterdHistoryByUnreturnedBooks(bookService.getFrom(books));	

		bookView.addObject("pageTitle", "BookCentric - Book entry");
		bookView.addObject("books", bookList);
		
		return bookView;
	}
	
	@GetMapping("/book/view/{id}")
	public ModelAndView viewBook(@PathVariable("id") Integer id) {
		ModelAndView bookView = new ModelAndView("book-update");
		
		Books book = bookService.getBy(id);
		List<Author> authorList = authorService.getAll();
		List<Publisher> publisherList = publisherService.getAll();
		List<Genre> genreList = genreService.getAll();
		
		bookView.addObject("pageTitle", "BookCentric - Book update");
		bookView.addObject("book", book);
		bookView.addObject("authorList", authorList);
		bookView.addObject("publisherList", publisherList);
		bookView.addObject("genreList", genreList);
		
		return bookView;
	}
	
	@PostMapping("/book/update")
	public String updateBook(Books book) {
		bookService.add(book);
		
		return "redirect:/book/inventory";
	}
}
