package com.bookcentric.blog;

import lombok.Data;

@Data
public class BlogResponse {
	private Integer id;
	private String subject;
	private String content;
	private String createdAtText;
	private String userFullName;
	private boolean own;
	private boolean admin;
	private boolean published;
	
}
