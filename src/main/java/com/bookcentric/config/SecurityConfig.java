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
				.antMatchers("/", "/home", "/user/registration", "/user/add", "/blogs/all/**").permitAll()
				.antMatchers("/treasury/**", "/open/**").permitAll()
				.antMatchers("/subscription/view/**").permitAll()
				.antMatchers("/book/get/**").permitAll()
				.antMatchers("/get/image/**", "/reading-challenge/**", "/reading-challenge/all", "/reading-challenge/image/**").permitAll()
				.antMatchers("/management/**").hasRole("ADMIN")
				.antMatchers("/user/get/**", "/user/update", "/user/history/**", "/testimonials/**").hasRole("ADMIN")
				.antMatchers("/book/entry", "/book/add", "/book/inventory", "/book/view/**", "/book/update/**", "/book/delete/**").hasRole("ADMIN")
				.antMatchers("/author/**", "/genre/**", "/tag/**", "/publisher/**", "/reading-challenge/management/**", "/events/me/**").hasRole("ADMIN")
				.antMatchers("/borrowlimit/**", "/subscriptionduration/**", "/category/**", "/deliveryarea/**", "/paymentmode/**").hasRole("ADMIN")
				.antMatchers("/user/update/wishlist", "/user/update/readingqueue", "/user/dashboard/", "/user/me/**").hasAnyRole("ADMIN", "USER")
				.antMatchers("/css/**", "/images/**", "/js/**", "/fonts/**").permitAll()
				.antMatchers("/donate-books/**", "/gift-subscription/**", "/coming-soon/**", "/reviews/other/**", "/events/all/**").permitAll()
				.antMatchers("/reviews/self/**", "/blogs/me/**").hasAnyRole("ADMIN", "USER")
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
