package com.bookcentric.component.treasury;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
		}

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



		Response response = new Response();
		response.setSuccess(true);
		response.setData(bookList);

		return response;
	}
}
