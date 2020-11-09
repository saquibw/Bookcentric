package com.bookcentric.component.user.dashboard;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.bookcentric.component.user.User;
import com.bookcentric.component.user.UserDTO;
import com.bookcentric.component.user.history.UserHistory;
import com.bookcentric.component.user.history.UserHistoryDto;
import com.bookcentric.component.user.security.UserSecurityService;
import com.bookcentric.custom.util.AppUtil;

@Controller
public class UserDashboardController {
	
	@Autowired UserSecurityService userSecurityService;
	@Autowired ModelMapper mapper;
	
	@GetMapping("/user/dashboard")
	public ModelAndView viewDashboard() {
		
		ModelAndView dashBoardView = new ModelAndView("user-dashboard");
		
		User user = userSecurityService.getLoggedInUser();
		
		String dateFormat = "dd MMM, yyyy";
		
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
		userDto.setDateOfRenewal(AppUtil.updateLocalDateFormat(userDto.getDateOfRenewal(), dateFormat));
		
		dashBoardView.addObject("pageTitle", "Member Dashboard");
		dashBoardView.addObject("user", userDto);
		
		return dashBoardView;
	}
}
