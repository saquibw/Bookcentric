package com.bookcentric.component.publisher;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="publisher")
public class Publisher {
	
	@Id @GeneratedValue
	private Integer id;
	private String name;

}
