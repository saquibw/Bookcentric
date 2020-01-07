package com.bookcentric.readingchallenge;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="reading_challenge_books")
public class ReadingChallengeBook {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String author;
	private String url;
	
	@ManyToOne @JoinColumn(name="readingChallengeId")
	private ReadingChallenge readingChallenge;
}
