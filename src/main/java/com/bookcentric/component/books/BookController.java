package com.bookcentric.component.books;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.bookcentric.component.books.author.Author;
import com.bookcentric.component.books.author.AuthorService;
import com.bookcentric.component.books.genre.Genre;
import com.bookcentric.component.books.genre.GenreService;
import com.bookcentric.component.books.publisher.Publisher;
import com.bookcentric.component.books.publisher.PublisherService;
import com.bookcentric.component.user.UserService;
import com.bookcentric.custom.util.Response;

@Controller
public class BookController {	
	
	@Autowired BookService bookService;
	@Autowired AuthorService authorService;
	@Autowired PublisherService publisherService;
	@Autowired GenreService genreService;
	@Autowired ModelMapper mapper;
	@Autowired HttpServletResponse response;
	@Autowired UserService userService;
	
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
	public String addBooks(Books book, @RequestParam("file") MultipartFile file) throws IOException {
		bookService.add(book);
		
		if(file != null && file.getSize() > 0) {
			bookService.storeImage(file, book.getId());
		}	
		
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
	public String updateBook(Books book, @RequestParam("file") MultipartFile file) throws IOException {
		bookService.add(book);
		
		if(file != null && file.getSize() > 0) {
			bookService.storeImage(file, book.getId());
		}
		
		return "redirect:/book/inventory";
	}
	
	@GetMapping("/get/image/{id}")
	@ResponseBody
	public void getImage(@PathVariable Integer id) throws SQLException, IOException {
		response.setContentType("image/jpeg");
		
		byte[] image = bookService.getImageBy(id);
		if(image != null) {
			
			ServletOutputStream stream = response.getOutputStream();
			stream.write(image);
			stream.flush();
			stream.close();
		}		
	}
	
	@ResponseBody
	@RequestMapping(value="/book/delete", method=RequestMethod.POST)
	public Response deleteBook(@RequestParam("id") Integer id) {
		Books book = bookService.getBy(id);
		bookService.delete(book);
		
		Response response = new Response();
		response.setSuccess(true);
		
		return response;
	}
	
	@ResponseBody
	@GetMapping("/book/search/readingqueue")
	public Response getBooks(@RequestParam("searchText") String searchText) {
		List<Books> books = bookService.searchByBookName(searchText);
		
		
		Response response = new Response();
		response.setData(books);
		response.setSuccess(true);
		
		return response;
	}
	
	
}
