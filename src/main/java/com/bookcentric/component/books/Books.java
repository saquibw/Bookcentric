package com.bookcentric.component.books;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="books")
public class Books {
	
	@Id @GeneratedValue
	private Integer id;
	private String name;
	private String year;
	private Integer noOfPages;
	
	
}
