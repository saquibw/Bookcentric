package com.bookcentric.component.utils;

import java.time.LocalDate;

public interface UtilService {
	public String getAlphaNumericString(int n);
	
	public String encryptPassword(String password);
	
	public boolean matchPassword(String newPassword, String oldPassword);
	
	public LocalDate format(LocalDate date, String pattern);
}
