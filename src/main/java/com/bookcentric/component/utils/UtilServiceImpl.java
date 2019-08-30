package com.bookcentric.component.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UtilServiceImpl implements UtilService {
	
	@Autowired private PasswordEncoder passwordEncoder;

	@Override
	public String getAlphaNumericString(int n) { 
  
        // chose a Character random from this String 
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                                    + "0123456789"
                                    + "abcdefghijklmnopqrstuvxyz"; 
  
        // create StringBuffer size of AlphaNumericString 
        StringBuilder sb = new StringBuilder(n); 
  
        for (int i = 0; i < n; i++) { 
  
            // generate a random number between 
            // 0 to AlphaNumericString variable length 
            int index 
                = (int)(AlphaNumericString.length() 
                        * Math.random()); 
  
            // add Character one by one in end of sb 
            sb.append(AlphaNumericString 
                          .charAt(index)); 
        } 
  
        return sb.toString(); 
    }

	@Override
	public String encryptPassword(String password) {
		return passwordEncoder.encode(password);
	}

	@Override
	public String decryptPassword(String password) {
		// TODO Auto-generated method stub
		return password;
	}

	@Override
	public LocalDate format(LocalDate date, String pattern) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		String dateString = date.toString();
		System.out.println(dateString);
		System.out.println(LocalDate.parse(dateString, formatter));
		return LocalDate.parse(dateString);
	} 
}
