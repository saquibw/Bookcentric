package com.bookcentric.component.user;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	public void add(User user) throws MySQLIntegrityConstraintViolationException{
		
		userRepository.save(user);
		
	}

	@Override
	public List<User> getAll() {
		
		return userRepository.findAll();
	}

	@Override
	public Optional<User> getBy(Integer id) {
		Optional<User> user = userRepository.findById(id);
		return user;
	}

	@Override
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
	}	
}
