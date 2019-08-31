package com.bookcentric.component.user;

import java.util.List;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

public interface UserService {
	public void add(User user) throws MySQLIntegrityConstraintViolationException;
	
	public List<User> getAll();
	
	public User getBy(Integer id);
	
	public User getByEmail(String email);
	
	public UserLoginDTO getBy(String email);
	
	public boolean sendUserActivationEmail(User user);
	
	public boolean sendUserStatusUpdateEmail(User user);
	
	public boolean sendUserRegistrationEmail(User user);
	
	public void sendSubscriptionExpiryEmail();
	
	public void renewSubscriptionDate();
}
