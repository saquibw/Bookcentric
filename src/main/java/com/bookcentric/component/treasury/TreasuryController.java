package com.bookcentric.component.treasury;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bookcentric.component.books.BookService;
import com.bookcentric.component.books.Books;
import com.bookcentric.component.books.BooksDTO;
import com.bookcentric.component.user.User;
import com.bookcentric.component.user.UserService;
import com.bookcentric.component.user.security.UserSecurityService;
import com.bookcentric.custom.util.Constants;
import com.bookcentric.custom.util.Response;

@Controller
public class TreasuryController {

	@Autowired BookService bookService;
	@Autowired ModelMapper mapper;
	@Autowired UserService userService;
	@Autowired UserSecurityService userSecurityService;

	@GetMapping("/treasury")
	public ModelAndView getTreasuryView() {
		ModelAndView treasuryView = new ModelAndView("treasury");

		treasuryView.addObject("pageTitle", "BookCentric - Treasury");
		
		return treasuryView;
	}

	@ResponseBody
	@GetMapping("/treasury/get/books/special")
	public Response getBooks(@RequestParam String type) {
		List<Books> books = new ArrayList<>();
		List<BooksDTO> bookList = new ArrayList<>();

		if(Constants.TYPE_BEST_SELLER.equals(type)) {
			books = bookService.getBestSellerBooks();
		} else if(Constants.TYPE_NEW_ARRIVAL.equals(type)) {
			books = bookService.getNewArrivalBooks();
		} else if(Constants.TYPE_CHILDREN.equals(type)) {
			books = bookService.getChildrensBooks();
		} else if(Constants.TYPE_READING_CHALLENGE.equals(type)) {
			books = bookService.getReadingChallengeBooks();
		}
		
		if(books != null && books.size() > 0) {
			bookList = books
					.stream()
					.sorted(Comparator.comparing(Books::getId).reversed())
					.map(b -> mapper.map(b, BooksDTO.class))
					.collect(Collectors.toList());
		}

		Response response = new Response();
		response.setSuccess(true);
		response.setData(bookList);

		return response;
	}
	
	@ResponseBody
	@GetMapping("/treasury/get/books/all")
	public Response getAllBooks(@RequestParam Integer pageNumber, @RequestParam String searchText, @RequestParam String searchTag) {
		Response response = new Response();
		
		Page<Books> books = null;
		if(!searchTag.isEmpty()) {
			books = bookService.getAllBySearchTag(pageNumber, Constants.BOOKS_COUNT_PER_PAGE, searchTag);
		} else {
			books = bookService.getAllByPageAndSort(pageNumber, Constants.BOOKS_COUNT_PER_PAGE, searchText);
		}
		
		if(books != null && books.getSize() > 0) {
			List<BooksDTO> bookList = new ArrayList<>();
			
			User user = userSecurityService.getLoggedInUser();
			
			books.forEach(b -> {
				BooksDTO book = mapper.map(b, BooksDTO.class);
				if(user != null) {
					if(user.getReadingQueue().contains(b)) {
						book.setReadingQueue(true);				
					}
					if(user.getWishlist().contains(b)) {
						book.setWishlist(true);				
					}
				}
				bookList.add(book);
			});
			
			Map<String, Object> data = new HashMap<>();
			data.put("bookList", bookList);
			data.put("totalBookCount", books.getTotalElements());
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
	
	@ResponseBody
	@GetMapping("/treasury/get/books/search/all")
	public Response getAllBooksBySearchCriteria(@RequestParam String searchText) {
		Response response = new Response();
		Map<String, Object> data = new HashMap<>();
		data.put("bookList", bookService.getAllBySearchCriteria(searchText));
		response.setSuccess(true);
		response.setDataMap(data);
		
		return response;
	}
}
