package com.bookcentric.component.subscription.category;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="category")
public class Category {

	@Id @GeneratedValue
	private Integer id;
	private String name;
	private String description;
	private boolean underAge;
}
