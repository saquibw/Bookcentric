package com.bookcentric.component.user.history;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.modelmapper.ModelMapper;
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
import com.bookcentric.component.user.UserDTO;
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
		
		ModelMapper mapper = new ModelMapper();
		UserDTO userDto = mapper.map(user, UserDTO.class);
		/*DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withLocale(Locale.US).withZone(ZoneId.systemDefault());
		userDto.getUserHistory().forEach(u -> {
			if(u.getIssueDate() != null) {
				u.setIssueDate(formatter.format(u.getIssueDate());
			}
			
		});*/
		
		model.addObject("user", userDto);
		
		return model;
	}
	
	@PostMapping("/user/history/issue")
	@ResponseBody
	public Response issueBook(@RequestParam int userId, @RequestParam String bookList) throws JSONException {
		User user = userService.getBy(userId).get();
		JSONArray arr = new JSONArray(bookList);
		
		List<UserHistory> userHistoryList = new ArrayList<>();
		
		for(int i=0; i < arr.length(); i++) {
			int bookId = arr.getInt(i);
			Books book = bookService.getBy(bookId);
			user.getReadingQueue().remove(book);
			
			UserHistory history = new UserHistory();
			history.setBooks(book);
			history.setUser(user);
			userHistoryList.add(history);
		}
		userService.add(user);
		userHistoryService.addAll(userHistoryList);
		
		Response response = new Response();
		response.setSuccess(true);
		return response;
	}
}
