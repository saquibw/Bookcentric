package com.bookcentric.component.user;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface UserService {
	public void add(User user);
	
	public List<User> getAll();
	
	public User getBy(Integer id);
	
	public User getByEmail(String email);
	
	public UserLoginDTO getBy(String email);
	
	public boolean sendUserActivationEmail(User user);
	
	public boolean sendUserStatusUpdateEmail(User user);
	
	public boolean sendUserRegistrationEmail(User user);
	
	public void sendSubscriptionExpiryEmail();
	
	public void renewSubscriptionDate();
	
	public boolean sendUserPasswordResetEmail(User user, String password);
	
	public User getByMembershipId(String id);
	
	public void storeImage(MultipartFile file, Integer id) throws IOException;
	
	public byte[] getImageBy(Integer id);
}
