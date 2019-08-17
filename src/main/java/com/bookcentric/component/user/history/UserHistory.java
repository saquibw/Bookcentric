package com.bookcentric.component.user.history;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.bookcentric.component.books.Books;
import com.bookcentric.component.user.User;

import lombok.Data;

@Data
@Entity
@Table(name="reading_history")
public class UserHistory {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private LocalDate issueDate;
	private LocalDate dueDate;
	private LocalDate returnDate;
	private boolean reissue;
	private String remarks;
	
	@ManyToOne @JoinColumn(name="userId")
	private User user;
	
	@ManyToOne @JoinColumn(name="bookId")
	private Books books;
}
