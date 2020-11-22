package com.bookcentric.blog;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.bookcentric.component.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Table(name="blog_comment")
public class BlogComments {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String comment;
	
	@CreationTimestamp
	private LocalDateTime createdAt;
	
	@UpdateTimestamp
	private LocalDateTime modifiedAt;
	
	@ManyToOne @JoinColumn(name="blogId") @JsonIgnore
	private Blog blog;
	
	@ManyToOne @JoinColumn(name="userId") @JsonIgnore
	private User user;
	
	public String getModifiedAtText() {
		return createdAt.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
	}
	
	public String getCommentUserName() {
		return user.getFullName();
	}
	
	public Integer getCommentUserId() {
		return user.getId();
	}

}
