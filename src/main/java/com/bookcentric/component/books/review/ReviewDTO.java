package com.bookcentric.component.books.review;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ReviewDTO {
	private Integer userId;
	private String userFullName;
	private Integer bookId;
	private Integer rating;
	private String comment;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;
}
