package com.bookcentric.component.books.publisher;

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
@Table(name="publisher")
public class Publisher {
	
	@Id @GeneratedValue
	private Integer id;
	private String name;
	
	@OneToMany(mappedBy = "publisher")
	private List<Books> books;

}
