package com.bookcentric.component.books;

import java.util.List;
import java.util.StringJoiner;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.bookcentric.component.books.author.Author;
import com.bookcentric.component.books.genre.Genre;
import com.bookcentric.component.books.publisher.Publisher;
import com.bookcentric.component.books.tag.Tag;
import com.bookcentric.component.user.history.UserHistory;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Table(name="books")
public class Books {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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
	private boolean children;
	private boolean readingChallenge;
	
	@ManyToMany
	@JoinTable(name = "book_author_pair", joinColumns = @JoinColumn(name = "bookId"), inverseJoinColumns = @JoinColumn(name = "authorId"))
	private List<Author> author;
	
	@ManyToMany
	@JoinTable(name = "book_genre_pair", joinColumns = @JoinColumn(name = "bookId"), inverseJoinColumns = @JoinColumn(name = "genreId"))
	private List<Genre> genre;
	
	@ManyToMany
	@JoinTable(name = "book_publisher_pair", joinColumns = @JoinColumn(name = "bookId"), inverseJoinColumns = @JoinColumn(name = "publisherId"))
	private List<Publisher> publisher;
	
	@ManyToMany
	@JoinTable(name = "book_tag_pair", joinColumns = @JoinColumn(name = "bookId"), inverseJoinColumns = @JoinColumn(name = "tagId"))
	private List<Tag> tag;
	
	@OneToMany(mappedBy="books") @JsonIgnore
	private List<UserHistory> userHistory;
	
	public String getAuthorName() {
		String name = "";
		List<Author> authorList = getAuthor();
		if(authorList.size() > 0) {
			StringJoiner joiner = new StringJoiner(" And ");
			authorList.forEach(a -> {
				joiner.add(a.getName());
			});
			name = joiner.toString();
		}
		
		return name;		
	}
	
	public String getGenreName() {
		String name = "";
		List<Genre> genreList = getGenre();
		if(genreList.size() > 0) {
			StringJoiner joiner = new StringJoiner(" And ");
			genreList.forEach(g -> {
				joiner.add(g.getName());
			});
			name = joiner.toString();
		}
		return name;
	}
	
	public String getPublisherName() {
		String name = "";
		List<Publisher> publisherList = getPublisher();
		if(publisherList.size() > 0) {
			StringJoiner joiner = new StringJoiner(" And ");
			publisherList.forEach(p -> {
				joiner.add(p.getName());
			});
			name = joiner.toString();
		}
		return name;
	}
	
	public String getTagName() {
		String name = "";
		List<Tag> tagList = getTag();
		if(tagList.size() > 0) {
			StringJoiner joiner = new StringJoiner(", ");
			tagList.forEach(t -> {
				joiner.add(t.getName());
			});
			name = joiner.toString();
		}
		return name;
	}
}
