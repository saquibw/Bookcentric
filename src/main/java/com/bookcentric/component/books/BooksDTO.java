package com.bookcentric.component.books;

import java.util.List;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.bookcentric.component.books.author.Author;
import com.bookcentric.component.books.genre.Genre;
import com.bookcentric.component.books.publisher.Publisher;
import com.bookcentric.component.user.history.UserHistory;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class BooksDTO {
	private Integer id;
	private String name;
	private String year;
	private Integer noOfPages;
	private String code;
	private String blurb;
	private String type;
	private String goodreadsLink;
	private Integer count;
	private Integer issuedCount;
	private Integer remainingCount;
	@JsonIgnore private List<UserHistory> userHistory;
	private boolean readingQueue;
	private boolean wishlist;
	private String authorName;
	private String genreName;
	private String publisherName;
	private String tagName;

}
