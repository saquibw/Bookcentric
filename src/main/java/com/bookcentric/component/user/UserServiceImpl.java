package com.bookcentric.component.user;

import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.bookcentric.component.utils.EmailService;
import com.bookcentric.config.AppConfig;
import com.bookcentric.custom.util.Constants;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired private UserRepository userRepository;
	
	@Autowired EmailService emailService;
	
	@Autowired AppConfig config;
	
	@Autowired ModelMapper mapper;
	
	public void add(User user) {		
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
		text.append(String.format("Dear %s", user.getFullName()));
		text.append("\n\n");
		text.append(String.format("Your account has been activated. Please use password: %s to login here: %s", user.getPassword(), "https://bookcentricbd.com/login"));
		
		emailService.sendSimpleEmail(to, subject, text.toString());
		
		return true;
	}

	@Override
	public boolean sendUserStatusUpdateEmail(User user) {
		String to = user.getEmail();
		String subject = "Your account status has been updated";
		
		StringBuilder text = new StringBuilder();
		text.append(String.format("Dear %s", user.getFullName()));
		text.append("\n\n");
		text.append(String.format("Your account status has been changed to : %s", user.getStatus().getName()));
		
		emailService.sendSimpleEmail(to, subject, text.toString());
		
		return true;
	}

	@Override
	public boolean sendUserRegistrationEmail(User user) {
		String to = config.getEmailRecipient();
		String subject = "A new user has been registered";
		
		StringBuilder text = new StringBuilder();
		text.append(String.format("User %s (%s) has completed registration. Please click below to check.", user.getFullName(), user.getEmail()));
		text.append("\n\n");
		text.append("http://bookcentricbd.com/management/user");
		
		emailService.sendSimpleEmail(to, subject, text.toString());
		
		return true;
	}

	@Async
	@Override
	@Transactional
	public void sendSubscriptionExpiryEmail() {
		List<User> userList = userRepository.findByActiveUserAndSubscriptionExpiry(Constants.EXPIRY_AFTER_DAYS);
		
		if(userList != null && userList.size() > 0) {
			userList.forEach(user -> {
				String to = user.getEmail();
				String subject = "Your subscription will expire soon";
				String subscriptionPlanName = user.getSubscription().getCategory().getName();
				String expiryDate = user.getDateOfRenewal().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy"));
				
				StringBuilder text = new StringBuilder();
				text.append(String.format("Dear %s", user.getFullName()));
				text.append("\n\n");
				text.append(String.format("Your subscription for the plan %s is going to expire on %s. Please contact bookcentric admin for service renewal.", subscriptionPlanName, expiryDate));
				
				emailService.sendSimpleEmail(to, subject, text.toString());
			});
		}
	}

	@Async
	@Override
	@Transactional
	public void renewSubscriptionDate() {
		List<User> expiredUsers = userRepository.findAllActiveExpiresToday();
		
		if(expiredUsers != null && expiredUsers.size() > 0) {
			expiredUsers.forEach(u -> {
				int subscriptionDurationInMonths = u.getSubscription().getSubscriptionDuration().getDurationInDays() / 30;
				u.setDateOfRenewal(u.getDateOfRenewal().plusMonths(subscriptionDurationInMonths));
			});
			userRepository.saveAll(expiredUsers);
		}
		
	}

	@Override
	public UserLoginDTO getBy(String email) {
		User user = userRepository.getByEmail(email);
		UserLoginDTO userDto = mapper.map(user, UserLoginDTO.class);
		
		return userDto;
	}

	@Override
	public User getByEmail(String email) {
		return userRepository.getByEmail(email);
	}
	
	@Override
	public boolean sendUserPasswordResetEmail(User user, String password) {
		String to = user.getEmail();
		String subject = "Your password has been reset";
		
		StringBuilder text = new StringBuilder();
		text.append(String.format("Dear %s", user.getFullName()));
		text.append("\n\n");
		text.append(String.format("Your password has been reset by admin. Please use password: %s to login here: %s", password, "https://bookcentricbd.com/login"));
		
		emailService.sendSimpleEmail(to, subject, text.toString());
		
		return true;
	}

}
