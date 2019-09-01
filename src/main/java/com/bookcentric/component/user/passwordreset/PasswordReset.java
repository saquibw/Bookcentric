package com.bookcentric.component.user.passwordreset;

import lombok.Data;

@Data
public class PasswordReset {
	private String currentPassword;
	private String newPassword;
	private String retypeNewPassword;
	private Integer userId;
}
