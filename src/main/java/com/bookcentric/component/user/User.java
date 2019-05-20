package com.bookcentric.component.user;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.bookcentric.component.books.Books;
import com.bookcentric.component.user.parent.Parent;
import com.bookcentric.component.user.subscription.Subscription;

import lombok.Data;

@Entity
@Data
@Table(name="user")
public class User {
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String firstName;
	private String middleName;
	private String lastName;
	private String email;
	private String dateOfBirth;
	private Integer phone;
	private String address;
	private String occupation;
	private String gender;
	private String password;
	private String membershipId;
	private Instant dateOfJoining;
	private Instant dateOfRenewal;
	private String status;
	private String deliveryArea;
	private String paymentMode;
	private String genre;
	
	@ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="subscriptionId")
	private Subscription subscription;
	
	@OneToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL) @JoinColumn(name="parentId", referencedColumnName = "id", nullable=true)
	private Parent parent;
	
	@ManyToMany
	@JoinTable(name = "reading_queue", joinColumns = @JoinColumn(name = "userId"), inverseJoinColumns = @JoinColumn(name = "bookId"))
	private List<Books> readingQueue;
}
