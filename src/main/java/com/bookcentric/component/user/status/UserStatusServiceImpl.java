package com.bookcentric.component.user.status;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserStatusServiceImpl implements UserStatusService {
	
	@Autowired UserStatusRepository repository;

	@Override
	public List<UserStatus> findAll() {
		
		return repository.findAll();
	}
	
	public UserStatus getBy(Integer id) {
		return repository.getOne(id);
	}

}
