package com.bookcentric.component.user.history;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserHistoryServiceImpl implements UserHistoryService {
	
	@Autowired private UserHistoryRepository repository;

	@Override
	public void add(UserHistory userHistory) {
		repository.save(userHistory);

	}

}
