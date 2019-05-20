package com.bookcentric.component.author;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="author")
public class Author {
	
	@Id @GeneratedValue
	private Integer id;
	private String name;

}
