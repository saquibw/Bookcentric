package com.bookcentric.component.utils;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bookcentric.component.user.UserLoginDTO;
import com.bookcentric.component.user.UserService;
import com.bookcentric.custom.util.Constants;

@Service
@Transactional
public class BookcentricUserDetailsService implements UserDetailsService{
	
	@Autowired UserService userService;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserLoginDTO user = userService.getBy(email);
		
		if(user == null) {
			throw new UsernameNotFoundException("No user found with username: "+ email);
		}
		System.out.println(user.toString());
		if(user.getStatus().getName().equals(Constants.STATUS_ACTIVE)) {
			List<String> roles = new ArrayList<>();

			boolean enabled = true;
	        boolean accountNonExpired = true;
	        boolean credentialsNonExpired = true;
	        boolean accountNonLocked = true;
	        	        
	        if(user.getRole().equals(Constants.ROLE_ADMIN)) {
	        	roles.add("ROLE_ADMIN");
	        } else {
	        	roles.add("ROLE_USER");
	        }
	        
	        return  new org.springframework.security.core.userdetails.User
	                (user.getEmail(), 
	                user.getPassword(), enabled, accountNonExpired, 
	                credentialsNonExpired, accountNonLocked, 
	                getAuthorities(roles));
		} 
		return null;
	}
	
	private static List<GrantedAuthority> getAuthorities (List<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }

}
