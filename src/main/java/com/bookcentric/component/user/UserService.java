package com.bookcentric.component.user;

import java.util.List;
import java.util.Optional;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

public interface UserService {
	public void add(User user) throws MySQLIntegrityConstraintViolationException;
	
	public List<User> getAll();
	
	public Optional<User> getBy(Integer id);
	
	public boolean updateStatus(Integer id, String status);
}
