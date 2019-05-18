package com.bookcentric.component.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	public void add(User user) {
		
		userRepository.save(user);
		
	}
}
