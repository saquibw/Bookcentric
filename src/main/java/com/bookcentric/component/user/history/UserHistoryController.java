package com.bookcentric.component.user.history;

import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bookcentric.component.books.BookService;
import com.bookcentric.component.books.Books;
import com.bookcentric.component.user.User;
import com.bookcentric.component.user.UserService;
import com.bookcentric.custom.util.Response;

@Controller
public class UserHistoryController {
	
	@Autowired private UserService userService;
	
	@Autowired private BookService bookService;
	
	@Autowired private UserHistoryService userHistoryService;

	@GetMapping("/user/history/{id}")
	public ModelAndView getUserHistoryDetails(@PathVariable("id") int id) {
		ModelAndView model = new ModelAndView("user-details-history");
		
		User user = userService.getBy(id).get();
		
		model.addObject("user", user);
		
		return model;
	}
	
	@PostMapping("/user/history/issue")
	@ResponseBody
	public Response issueBook(@RequestParam int userId, @RequestParam String bookList) throws JSONException {
		User user = userService.getBy(userId).get();
		JSONArray arr = new JSONArray(bookList);
		
		for(int i=0; i < arr.length(); i++) {
			int bookId = arr.getInt(i);
			Books book = bookService.getBy(bookId);
			user.getReadingQueue().remove(book);
			
			UserHistory history = new UserHistory();
			history.setBook(book);
			history.setUser(user);
			
			userHistoryService.add(history);
		}
		userService.add(user);
		
		Response response = new Response();
		response.setSuccess(true);
		return response;
	}
}
