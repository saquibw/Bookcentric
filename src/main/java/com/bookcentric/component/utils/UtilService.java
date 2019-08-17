package com.bookcentric.component.utils;

import java.time.LocalDate;

public interface UtilService {
	public String getAlphaNumericString(int n);
	
	public String encryptPassword(String password);
	
	public String decryptPassword(String password);
	
	public LocalDate format(LocalDate date, String pattern);
}
