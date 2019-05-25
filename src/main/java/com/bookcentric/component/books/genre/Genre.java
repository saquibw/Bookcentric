package com.bookcentric.component.books.genre;

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
@Table(name="genre")
public class Genre {
	@Id @GeneratedValue
	private Integer id;
	private String name;
	
	@OneToMany(mappedBy = "publisher")
	private List<Books> books;
}
