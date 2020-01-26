package com.bookcentric.blog;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
		return modifiedAt.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
	}
	
	public String getUserName() {
		return user.getFullName();
	}

}
