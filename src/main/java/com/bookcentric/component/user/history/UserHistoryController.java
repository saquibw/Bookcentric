package com.bookcentric.component.user.history;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import com.bookcentric.component.utils.UtilService;
import com.bookcentric.custom.util.AppUtil;
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
	
	@Autowired UtilService utils;

	@GetMapping("/user/history/{id}")
	public ModelAndView getUserHistoryDetails(@PathVariable("id") int id) {
		ModelAndView model = new ModelAndView("user-details-history");
		
		String dateFormat = "dd MMM, yy";
		
		User user = userService.getBy(id);
		List<UserHistoryDto> historyList = user.getUserHistory()
				.stream()
				.sorted(Comparator.comparing(UserHistory::getIssueDate).reversed())
				.map(u -> mapper.map(u, UserHistoryDto.class))
				.map(u -> {
					u.setIssueDate(AppUtil.updateLocalDateFormat(u.getIssueDate(), dateFormat));
					u.setDueDate(AppUtil.updateLocalDateFormat(u.getDueDate(), dateFormat));
					u.setReturnDate(u.getReturnDate() != null ? AppUtil.updateLocalDateFormat(u.getReturnDate(), dateFormat) : u.getReturnDate());
					
					return u;
				})
				.collect(Collectors.toList());
		
		UserDTO userDto = mapper.map(user, UserDTO.class);
		userDto.setUserHistory(historyList);
				
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
	
	@ResponseBody
	@PostMapping("/user/history/return")
	public Response returnBook(@RequestParam int historyId) {
		Response response = new Response();
		
		UserHistory history = userHistoryService.findBy(historyId);
		LocalDate returnDate = LocalDate.now();
		history.setReturnDate(returnDate);
		userHistoryService.add(history);
		
		Map<String, String> data = new HashMap<>();
		data.put("returnDate", returnDate.toString());
		response.success = true;
		response.setData(data);
		
		return response;
	}
	
	@ResponseBody
	@PostMapping("/user/history/reissue")
	public Response reissueBook(@RequestParam int historyId) {
		Response response = new Response();
		
		UserHistory history = userHistoryService.findBy(historyId);
		LocalDate returnDate = LocalDate.now();
		history.setReturnDate(returnDate);
		userHistoryService.add(history);
		
		Integer planDueInDays = history.getUser().getSubscription().getCategory().getPlanDuration().getDurationInDays();
		
		UserHistory reissueHistory = new UserHistory();
		reissueHistory.setReissue(true);
		reissueHistory.setIssueDate(returnDate);
		reissueHistory.setDueDate(returnDate.plusDays(planDueInDays));
		reissueHistory.setBooks(history.getBooks());
		reissueHistory.setUser(history.getUser());
		userHistoryService.add(reissueHistory);
		
		response.success = true;
		return response;
	}
}
