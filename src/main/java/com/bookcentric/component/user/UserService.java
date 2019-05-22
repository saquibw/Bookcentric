package com.bookcentric.component.user;

import java.util.List;
import java.util.Optional;

public interface UserService {
	public void add(User user);
	
	public List<User> getAll();
	
	public Optional<User> getBy(Integer id);
	
	public boolean updateStatus(Integer id, String status);
}
