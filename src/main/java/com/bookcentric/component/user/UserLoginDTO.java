package com.bookcentric.component.user;

import com.bookcentric.component.user.status.UserStatus;

import lombok.Data;

@Data
public class UserLoginDTO {
	private Integer id;
	private String email;
	private String password;
	private String role;
	private UserStatus status;
}
