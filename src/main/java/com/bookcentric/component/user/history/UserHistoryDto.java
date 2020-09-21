package com.bookcentric.component.user.history;

import com.bookcentric.component.books.Books;

import lombok.Data;

@Data
public class UserHistoryDto {
	private Integer id;
	private String issueDate;
	private String dueDate;
	private String returnDate;
	private boolean reissue;
	private String remarks;
	private Books books;
}
