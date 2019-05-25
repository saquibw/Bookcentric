package com.bookcentric.component.books.genre;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="genre")
public class Genre {
	@Id @GeneratedValue
	private Integer id;
	private String name;
}
