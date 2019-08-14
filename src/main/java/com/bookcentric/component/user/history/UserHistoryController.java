package com.bookcentric.component.user.history;

import java.time.LocalDate;
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
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class UserHistoryController {
	
	@Autowired private UserService userService;
	
	@Autowired private BookService bookService;
	
	@Autowired private UserHistoryService userHistoryService;
	
	@Autowired private ModelMapper mapper;

	@GetMapping("/user/history/{id}")
	public ModelAndView getUserHistoryDetails(@PathVariable("id") int id) {
		ModelAndView model = new ModelAndView("user-details-history");
		
		User user = userService.getBy(id);
		
		UserDTO userDto = mapper.map(user, UserDTO.class);
		
		model.addObject("user", userDto);
		
		return model;
	}
	
	@PostMapping("/user/history/issue")
	@ResponseBody
	public Response issueBook(@RequestParam int userId, @RequestParam String bookList) throws JSONException, MySQLIntegrityConstraintViolationException {
		User user = userService.getBy(userId);
		JSONArray arr = new JSONArray(bookList);
		
		if(user.getDateOfJoining() == null) {
			LocalDate today = LocalDate.now();
			
			log.debug("Issueing books for the first time for user {}", user.getFullName());
			Integer renewalInDays = user.getSubscription().getSubscriptionDuration().getDurationInDays();
			LocalDate renewalDate = today.plusDays(renewalInDays);
			
			user.setDateOfJoining(today);
			user.setDateOfRenewal(renewalDate);
		}
		
		Integer planDueInDays = user.getSubscription().getCategory().getPlanDuration().getDurationInDays();
		
		List<UserHistory> userHistoryList = new ArrayList<>();
		
		for(int i=0; i < arr.length(); i++) {
			int bookId = arr.getInt(i);
			Books book = bookService.getBy(bookId);
			user.getReadingQueue().remove(book);
			
			UserHistory history = new UserHistory();
			history.setBooks(book);
			history.setUser(user);
			history.setIssueDate(LocalDate.now());
			history.setDueDate(LocalDate.now().plusDays(planDueInDays));
			userHistoryList.add(history);
		}
		userService.add(user);
		userHistoryService.addAll(userHistoryList);
		
		userHistoryService.sendBookIssueEmail(userHistoryList, user);
		
		Response response = new Response();
		response.setSuccess(true);
		return response;
	}
	
	@GetMapping("/user/history/readingqueue/{id}")
	public ModelAndView getReadingQueue(@PathVariable("id") int userId) {
		ModelAndView view = new ModelAndView("reading-queue-update");
		
		User user = userService.getBy(userId);
		
		UserDTO userDto = mapper.map(user, UserDTO.class);
		
		view.addObject("user", userDto);
		
		return view;
	}
}
