package com.bookcentric.component.books;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.bookcentric.component.books.author.Author;
import com.bookcentric.component.books.genre.Genre;
import com.bookcentric.component.books.publisher.Publisher;

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
	private String code;
	private String blurb;
	
	@ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="authorId")
	private Author author;
	
	@ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="publisherId")
	private Publisher publisher;
	
	@ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="genreId")
	private Genre genre;
	
	
}
