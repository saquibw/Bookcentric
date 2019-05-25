package com.bookcentric.component.user.history;

import java.util.List;

public interface UserHistoryService {

	public void add(UserHistory userHistory);
	
	public void addAll(List<UserHistory> userHistoryList);
}
