package com.bookcentric.component.user.history;

import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookcentric.component.user.User;
import com.bookcentric.component.utils.EmailService;

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
		text.append(String.format("Dear %s %s %s", user.getFirstName(), user.getMiddleName(), user.getLastName()));
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

}
