package com.bookcentric.component.user.history;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.bookcentric.component.email.Email;
import com.bookcentric.component.email.EmailService;
import com.bookcentric.component.user.User;
import com.bookcentric.config.AppConfig;
import com.bookcentric.custom.util.AppUtil;
import com.bookcentric.custom.util.Constants;

@Service
public class UserHistoryServiceImpl implements UserHistoryService {
	
	@Autowired private UserHistoryRepository repository;
	
	@Autowired EmailService emailService;
	
	@Autowired AppConfig config;

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
		String subject = "Book(s) have been issued to you";
		
		StringBuilder text = new StringBuilder();
		text.append(String.format("Dear %s,", user.getFullName()));
		text.append("<br><br>");
		text.append("Following book(s) have been issued to you.");
		text.append("<br><br>");
		
		text.append("<table border='1'><tr><th>Book Name</th><th>Issue Date</th><th>Due Date</th></tr>");
		
		userHistoryList.forEach(u -> {
			text.append(getBookInfoText(u));
		});
		
		text.append("</table>");
		text.append("<br><br>");
		text.append(AppUtil.getEmailSignature());
		
		String message = text.toString();
		Email email = new Email(to, subject, message);
		
		try {
			File file = new ClassPathResource(Constants.URL_THINGS_TO_REMEMBER).getFile();
			Map<String, File> attachment = new HashMap<>();
			attachment.put(Constants.FILE_NAME_THINGS_TO_REMEMBER, file);
			email.setAttachments(attachment);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
				
		try {
			emailService.sendHtmlEmail(email);
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
			
			String subject = "Your borrowed book(s) due date will expire soon";
			
			historyMap.forEach((k, v) -> {
				User receiver = v.stream().findFirst().get().getUser();
				String to = receiver.getEmail();
				String message = preparePlanExpiryText(receiver, v);
				Email email = new Email(to, subject, message);
				
				String[] cc = new String[] {config.getEmailRecipient()};
				email.setCc(cc);
				
				try {
					emailService.sendHtmlEmail(email);
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			});
		}		
	}
	
	private String preparePlanExpiryText(User user, List<UserHistory> historyList) {
		StringBuilder text = new StringBuilder();
		
		String dueDateInString = historyList.get(0).getDueDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
		
		text.append(String.format("Dear %s,", user.getFullName()));
		text.append("<br><br>");
		text.append(String.format("This is a gentle reminder that the following book(s) are due for return on %s. ", dueDateInString));
		text.append("If you would like to re-issue the book(s), please let us know and we will do the needful. ");
		text.append("However, if you have re-issued the book(s) already and wish to keep it/those for another week, "
				+ "you may do so by paying only Tk.20 per book per  week. ");
		text.append("<br><br>");
		
		text.append("<table border='1'><tr><th>Book Name</th><th>Issue Date</th><th>Due Date</th></tr>");
		
		historyList.forEach(u -> {
			text.append(getBookInfoText(u));
		});
		
		text.append("</table>");
		text.append("<br><br>");
		text.append(AppUtil.getEmailSignature());
		
		return text.toString();
	}

	@Override
	public UserHistory findBy(Integer id) {
		return repository.getOne(id);
	}
	
	@Override
	public boolean sendBookReissueEmail(UserHistory userHistory) {
		User user = userHistory.getUser();
		String to = user.getEmail();
		String subject = "Book has been re-issued to you";
		
		StringBuilder text = new StringBuilder();
		text.append(String.format("Dear %s,", user.getFullName()));
		text.append("<br><br>");
		text.append("Following book has been re-issued to you.");
		text.append("<br><br>");
		
		text.append("<table border='1'><tr><th>Book Name</th><th>Issue Date</th><th>Due Date</th></tr>");
		
		text.append(getBookInfoText(userHistory));
		
		text.append("</table>");
		text.append("<br><br>");
		text.append(AppUtil.getEmailSignature());
		
		String message = text.toString();
		Email email = new Email(to, subject, message);
		
		try {
			emailService.sendHtmlEmail(email);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
		return true;
	}
	
	private String getBookInfoText(UserHistory history) {
		return String.format("<tr><td>%s</td><td>%s</td><td>%s</td></tr>", 
				history.getBooks().getName(), 
				history.getIssueDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)),
				history.getDueDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
	}
	
}
