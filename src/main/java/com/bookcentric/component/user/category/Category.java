package com.bookcentric.component.user.category;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name="category")
public class Category {
	@Id @GeneratedValue
	private Integer id;
	private String name;
	private Integer upperAge;
	private Integer lowerAge;
	private String description;
	private Boolean underAge;
}
