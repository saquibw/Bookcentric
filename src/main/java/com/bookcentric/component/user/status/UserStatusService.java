package com.bookcentric.component.user.status;

import java.util.List;

public interface UserStatusService {
	public List<UserStatus> findAll();
	public UserStatus getBy(Integer id);
}
