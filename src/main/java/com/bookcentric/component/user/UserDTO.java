package com.bookcentric.component.user;

import java.util.List;

import com.bookcentric.component.books.Books;
import com.bookcentric.component.user.history.UserHistory;

import lombok.Data;

@Data
public class UserDTO {
	private Integer id;
	private String firstName;
	private String middleName;
	private String lastName;
	private String email;
	private String membershipId;
	private List<Books> readingQueue;
	private List<UserHistory> userHistory;
}
