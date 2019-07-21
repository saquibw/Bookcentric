package com.bookcentric.component.user;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookcentric.component.utils.EmailService;
import com.bookcentric.component.utils.UtilService;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired private UserRepository userRepository;
	
	@Autowired EmailService emailService;
	
	public void add(User user) throws MySQLIntegrityConstraintViolationException{
		
		userRepository.save(user);
		
	}

	@Override
	public List<User> getAll() {
		
		return userRepository.findAll();
	}

	@Override
	public User getBy(Integer id) {
		User user = userRepository.getOne(id);
		return user;
	}

	@Override
	public boolean sendUserActivationEmail(User user) {
		String to = user.getEmail();
		String subject = "Your account has been activated";
		
		StringBuilder text = new StringBuilder();
		text.append(String.format("Dear %s %s %s", user.getFirstName(), user.getMiddleName(), user.getLastName()));
		text.append("\n\n");
		text.append(String.format("Your account has been activated. Please use password: %s to login here: %s", user.getPassword(), "https://bookcentric.com/login"));
		
		emailService.sendSimpleEmail(to, subject, text.toString());
		
		return true;
	}

	@Override
	public boolean sendUserStatusUpdateEmail(User user) {
		String to = user.getEmail();
		String subject = "Your account status has been updated";
		
		StringBuilder text = new StringBuilder();
		text.append(String.format("Dear %s %s %s", user.getFirstName(), user.getMiddleName(), user.getLastName()));
		text.append("\n\n");
		text.append(String.format("Your account status has been changed to : %s", user.getStatus().getName()));
		
		emailService.sendSimpleEmail(to, subject, text.toString());
		
		return true;
	}

	/*@Override
	public boolean updateStatus(Integer id, String status) {
		boolean success = false;
		Optional<User> u = userRepository.findById(id);
		if(u.isPresent()) {
			User user = u.get();
			user.setStatus(status);
			
			userRepository.save(user);
			success = true;
		}
		return success;
	}*/
}
