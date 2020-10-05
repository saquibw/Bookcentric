package com.bookcentric.component.user.history;

import java.util.List;

import com.bookcentric.component.user.User;

public interface UserHistoryService {

	public void add(UserHistory userHistory);
	
	public void addAll(List<UserHistory> userHistoryList);
	
	public boolean sendBookIssueEmail(List<UserHistory> userHistoryList, User user);
	
	public void sendPlanExpiryEmail();
	
	public UserHistory findBy(Integer id);
	
	public boolean sendBookReissueEmail(UserHistory userHistory);
}
