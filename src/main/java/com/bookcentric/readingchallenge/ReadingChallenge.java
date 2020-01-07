package com.bookcentric.readingchallenge;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="reading_challenge")
public class ReadingChallenge {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String month;
	private Integer year;
	private String subject;
	private boolean published;
	
	@OneToMany(mappedBy="readingChallenge")
	private List<ReadingChallengeBook> books;

}
