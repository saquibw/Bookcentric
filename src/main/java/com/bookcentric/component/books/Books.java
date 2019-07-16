package com.bookcentric.component.books;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.web.multipart.MultipartFile;

import com.bookcentric.component.books.author.Author;
import com.bookcentric.component.books.genre.Genre;
import com.bookcentric.component.books.publisher.Publisher;
import com.bookcentric.component.user.history.UserHistory;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Table(name="books")
public class Books {
	
	@Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer id;
	private String name;
	private String year;
	private Integer noOfPages;
	private String code;
	private String blurb;
	private String type;
	private String goodreadsLink;
	private Integer count;
	private boolean bestSeller;
	private boolean newArrival;
	//private MultipartFile imageFile;
	//private byte[] image;
	
	@ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="authorId") @JsonIgnore
	private Author author;
	
	@ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="publisherId") @JsonIgnore
	private Publisher publisher;
	
	@ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="genreId") @JsonIgnore
	private Genre genre;
	
	@OneToMany(mappedBy="books") @JsonIgnore
	private List<UserHistory> userHistory;
	
	
}
