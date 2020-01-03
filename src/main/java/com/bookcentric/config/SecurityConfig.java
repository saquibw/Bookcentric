package com.bookcentric.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bookcentric.component.utils.BookcentricUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired private BookcentricUserDetailsService userDetailsService;
	@Autowired private PasswordEncoder passwordEncoder;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.
			authorizeRequests()
				.antMatchers("/", "/home").permitAll()
				.antMatchers("/user/registration", "/user/add").permitAll()
				.antMatchers("/treasury/**").permitAll()
				.antMatchers("/subscription/view/**").permitAll()
				.antMatchers("/book/get/**").permitAll()
				.antMatchers("/get/image/**").permitAll()
				.antMatchers("/management/**").hasRole("ADMIN")
				.antMatchers("/user/get/**", "/user/update", "/user/history/**").hasRole("ADMIN")
				.antMatchers("/book/entry", "/book/add", "/book/inventory", "/book/view/**", "/book/update/**", "/book/delete/**").hasRole("ADMIN")
				.antMatchers("/author/**", "/genre/**", "/tag/**", "/publisher/**").hasRole("ADMIN")
				.antMatchers("/borrowlimit/**", "/subscriptionduration/**", "/category/**").hasRole("ADMIN")
				.antMatchers("/user/update/wishlist", "/user/update/readingqueue", "/user/dashboard/").hasAnyRole("ADMIN", "USER")
				.antMatchers("/css/**").permitAll()
				.antMatchers("/images/**").permitAll()
				.antMatchers("/js/**").permitAll()
				.antMatchers("/fonts/**").permitAll()
				.antMatchers("/reviews/other/**").permitAll()
				.antMatchers("/donate-books/**", "/gift-subscription/**").permitAll()
				.antMatchers("/reviews/self/**").hasAnyRole("ADMIN", "USER")
				.anyRequest().authenticated()
				.and()
			.formLogin()
				.loginPage("/login")
				.defaultSuccessUrl("/home",true)
				.successForwardUrl("/postLogin")
                .failureUrl("/loginFailed")
				.permitAll()
				.and()
			.logout()
				.logoutUrl("/logout")
				.logoutSuccessUrl("/logoutsuccess")
            	.invalidateHttpSession(true)
            	.deleteCookies("JSESSIONID")
            	.permitAll()
            	.and()
            	.csrf().disable();
	}	
	

}
