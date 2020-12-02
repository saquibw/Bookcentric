package com.bookcentric.component.books;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import com.bookcentric.component.books.tag.Tag;
import com.bookcentric.component.books.tag.TagService;
import com.bookcentric.component.user.User;
import com.bookcentric.component.user.UserService;
import com.bookcentric.component.user.security.UserSecurityService;
import com.bookcentric.custom.util.Constants;
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
	@Autowired UserSecurityService userSecurityService;
	@Autowired TagService tagService;

	@GetMapping("/book/entry")
	public ModelAndView viewBookEntry() {
		ModelAndView bookView = new ModelAndView("book-entry");

		Books book = new Books();
		List<Author> authorList = getSortedAuthorList();
		List<Publisher> publisherList = getSortedPublisherList();
		List<Genre> genreList = getSortedGenreList();
		List<Tag> tagList = getSortedTagList();

		bookView.addObject("pageTitle", "BookCentric - Book Entry");
		bookView.addObject("book", book);
		bookView.addObject("authorList", authorList);
		bookView.addObject("publisherList", publisherList);
		bookView.addObject("genreList", genreList);
		bookView.addObject("tagList", tagList);

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
	public ModelAndView getInventoryView() {
		ModelAndView bookView = new ModelAndView("book-inventory");

		bookView.addObject("pageTitle", "BookCentric - Book Inventory");

		return bookView;
	}

	@GetMapping("/book/view/{id}")
	public ModelAndView viewBook(@PathVariable("id") Integer id) {
		ModelAndView bookView = new ModelAndView("book-update");

		Books book = bookService.getBy(id);
		List<Author> authorList = getSortedAuthorList();
		List<Publisher> publisherList = getSortedPublisherList();
		List<Genre> genreList = getSortedGenreList();
		List<Tag> tagList = getSortedTagList();

		bookView.addObject("pageTitle", "BookCentric - Book Update");
		bookView.addObject("book", book);
		bookView.addObject("authorList", authorList);
		bookView.addObject("publisherList", publisherList);
		bookView.addObject("genreList", genreList);
		bookView.addObject("tagList", tagList);

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

	@GetMapping("/book/get/{id}")
	public ModelAndView getBookView(@PathVariable int id) {
		ModelAndView bookView = new ModelAndView("book");

		Books books = bookService.getBy(id);

		BooksDTO book = mapper.map(books, BooksDTO.class);
		bookService.updateCount(book);
		book.setAuthorName(books.getAuthorName());
		User user = userSecurityService.getLoggedInUser();

		if(user != null) {
			if(user.getReadingQueue().contains(books)) {
				book.setReadingQueue(true);				
			}
			if(user.getWishlist().contains(books)) {
				book.setWishlist(true);				
			}
		}
		
		bookView.addObject("book", book);
		bookView.addObject("pageTitle", "BookCentric - Book");

		return bookView;
	}

	@ResponseBody
	@GetMapping("/book/get/all")
	public Response getAllBooks(@RequestParam Integer pageNumber, @RequestParam String searchText) {
		Response response = new Response();
		
		Page<Books> books = bookService.getAllByPageAndSort(pageNumber, Constants.BOOKS_COUNT_PER_PAGE, searchText);
		
		if(books.getSize() > 0) {
			List<BooksDTO> bookList = bookService.getFilterdHistoryByUnreturnedBooks(bookService.getFrom(books.getContent()));
			
			Map<String, Object> data = new HashMap<>();
			data.put("bookList", bookList);
			data.put("totalCount", books.getTotalElements());
			data.put("isFirstPage", books.isFirst());
			data.put("isLastPage", books.isLast());
			data.put("totalPageCount", books.getTotalPages());
			data.put("hasNextPage", books.hasNext());
			data.put("hasPreviousPage", books.hasPrevious());
			data.put("currentPageNumber", books.getNumber());
			
			response.setSuccess(true);
			response.setDataMap(data);
		} else {
			response.setSuccess(false);
		}
		
		return response;
	}
	
	private List<Author> getSortedAuthorList() {
		return authorService.findAll().stream().sorted(Comparator.comparing(Author::getName)).collect(Collectors.toList());
	}
	
	private List<Publisher> getSortedPublisherList() {
		return publisherService.findAll().stream().sorted(Comparator.comparing(Publisher::getName)).collect(Collectors.toList());
	}
	
	private List<Genre> getSortedGenreList() {
		return genreService.findAll().stream().sorted(Comparator.comparing(Genre::getName)).collect(Collectors.toList());
	}
	
	private List<Tag> getSortedTagList() {
		return tagService.findAll().stream().sorted(Comparator.comparing(Tag::getName)).collect(Collectors.toList());
	}

}
