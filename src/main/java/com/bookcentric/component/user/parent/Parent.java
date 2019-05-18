package com.bookcentric.component.user.parent;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="parent")
public class Parent {
	
	@Id @GeneratedValue
	private Integer id;
	private String firstName;
	private String lastName;
	private String phone;
	private String relation;
	private String occupation;

}
