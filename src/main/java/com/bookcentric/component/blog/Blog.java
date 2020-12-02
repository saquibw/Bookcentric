package com.bookcentric.component.blog;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.bookcentric.component.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Table(name="blog")
public class Blog {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String subject;
	private String content;
	private boolean published;
	
	@CreationTimestamp
	private LocalDateTime createdAt;
	
	@UpdateTimestamp
	private LocalDateTime modifiedAt;
	
	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="userId")
	private User user;
	
	@OneToMany(mappedBy="blog")
	private List<BlogComments> comments;
	
	public String getUserName() {
		return user.getFullName();
	}
	
	public String getCreatedAtText() {
		return createdAt.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
	}
	
	public Integer getUserId() {
		return user.getId();
	}
}
