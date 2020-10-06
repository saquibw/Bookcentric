package com.bookcentric.component.user.testimonial;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="testimonial")
public class Testimonial {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String authorName;
	private String description;
	private boolean published;
}
