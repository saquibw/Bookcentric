package com.bookcentric.component.author;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.bookcentric.component.books.Books;

import lombok.Data;

@Data
@Entity
@Table(name="author")
public class Author {
	
	@Id @GeneratedValue
	private Integer id;
	private String name;
	
	@OneToMany(mappedBy = "author")
	private List<Books> books;

}
