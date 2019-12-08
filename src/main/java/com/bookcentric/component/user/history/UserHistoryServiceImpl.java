package com.bookcentric.component.user.history;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.bookcentric.component.user.User;
import com.bookcentric.component.utils.EmailService;
import com.bookcentric.custom.util.Constants;

@Service
public class UserHistoryServiceImpl implements UserHistoryService {
	
	@Autowired private UserHistoryRepository repository;
	
	@Autowired EmailService emailService;

	@Override
	public void add(UserHistory userHistory) {
		repository.save(userHistory);

	}
	
	@Override
	public void addAll(List<UserHistory> userHistoryList) {
		repository.saveAll(userHistoryList);
	}

	@Override
	public boolean sendBookIssueEmail(List<UserHistory> userHistoryList, User user) {
		String to = user.getEmail();
		String subject = "Books have been issued to you";
		
		StringBuilder text = new StringBuilder();
		text.append(String.format("Dear %s", user.getFullName()));
		text.append("<br><br>");
		text.append("Following books have been issued to you.");
		text.append("<br><br>");
		
		text.append("<table border='1'><tr><th>Book name</th><th>Issue date</th><th>Due date</th></tr>");
		
		userHistoryList.forEach(u -> {
			text.append(String.format("<tr><td>%s</td><td>%s</td><td>%s</td></tr>", u.getBooks().getName(), u.getIssueDate(), u.getDueDate()));
		});
		
		text.append("</table>");
		
		try {
			emailService.sendHtmlEmail(to, subject, text.toString());
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
		return true;
	}

	@Async
	@Override
	public void sendPlanExpiryEmail() {
		List<UserHistory> historyList = repository.findByActiveUserAndPlanExpiry(Constants.EXPIRY_AFTER_DAYS);
		Map<Integer, List<UserHistory>> historyMap = new HashMap<Integer, List<UserHistory>>();

		if(historyList != null && historyList.size() > 0) {		
			historyList.forEach(history -> {
				User user = history.getUser();
				if(historyMap.containsKey(user.getId())) {
					historyMap.get(user.getId()).add(history);
				} else {
					List<UserHistory> historyListForSingleUser = new ArrayList<>();
					historyListForSingleUser.add(history);
					historyMap.put(user.getId(), historyListForSingleUser);
				}
			});
			
			String subject = "Books due date will expire soon";
			
			historyMap.forEach((k, v) -> {
				User receiver = v.stream().findFirst().get().getUser();
				String to = receiver.getEmail();
				String emailText = preparePlanExpiryText(receiver, v);
				
				try {
					emailService.sendHtmlEmail(to, subject, emailText);
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			});
		}		
	}
	
	private String preparePlanExpiryText(User user, List<UserHistory> historyList) {
		StringBuilder text = new StringBuilder();
		
		text.append(String.format("Dear %s", user.getFullName()));
		text.append("<br><br>");
		text.append("This is a gentle reminder that following book(s) are going to expire soon. ");
		text.append("However, if you wish to keep them for another week, you may do so by paying only Tk.20 per week.");
		text.append("<br><br>");
		
		text.append("<table border='1'><tr><th>Book name</th><th>Issue date</th><th>Due date</th></tr>");
		
		historyList.forEach(u -> {
			text.append(String.format("<tr><td>%s</td><td>%s</td><td>%s</td></tr>", u.getBooks().getName(), u.getIssueDate(), u.getDueDate()));
		});
		
		text.append("</table>");
		
		return text.toString();
	}

	@Override
	public UserHistory findBy(Integer id) {
		return repository.getOne(id);
	}
	
}
