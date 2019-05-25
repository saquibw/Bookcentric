package com.bookcentric.component.books;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
		
		bookView.addObject("books", book);
		bookView.addObject("authorList", authorList);
		bookView.addObject("publisherList", publisherList);
		bookView.addObject("genreList", genreList);
		
		return bookView;
	} 
	
	@PostMapping("/book/add")
	public String addBooks(Books book) {
		//System.out.println(book.getGenre());
		bookService.add(book);
		
		return "redirect:/book/entry";
	}
}
