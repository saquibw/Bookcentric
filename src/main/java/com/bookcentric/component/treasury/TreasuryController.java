package com.bookcentric.component.treasury;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bookcentric.component.books.BookService;
import com.bookcentric.component.books.Books;
import com.bookcentric.custom.util.Constants;
import com.bookcentric.custom.util.Response;

@Controller
public class TreasuryController {
	
	@Autowired BookService bookService;

	@GetMapping("/treasury")
	public ModelAndView getTreasuryView() {
		ModelAndView treasuryView = new ModelAndView("treasury");
		
		return treasuryView;
	}
	
	@ResponseBody
	@GetMapping("/treasury/get/books")
	public Response getBooks(@RequestParam String type) {
		List<Books> books = new ArrayList<>();
		
		if(Constants.TYPE_BEST_SELLER.equals(type)) {
			books = bookService.getBestSellerBooks();
		} else if(Constants.TYPE_NEW_ARRIVAL.equals(type)) {
			books = bookService.getNewArrivalBooks();
		}
		
		Response response = new Response();
		response.setSuccess(true);
		response.setData(books);
		
		return response;
	}
}
