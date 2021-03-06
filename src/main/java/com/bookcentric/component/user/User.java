package com.bookcentric.component.user;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.bookcentric.component.books.Books;
import com.bookcentric.component.subscription.Subscription;
import com.bookcentric.component.user.history.UserHistory;
import com.bookcentric.component.user.history.UserHistoryDto;
import com.bookcentric.component.user.parent.Parent;
import com.bookcentric.component.user.status.UserStatus;

import lombok.Data;

@Entity
@Data
@Table(name="user")
public class User {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String firstName;
	private String middleName;
	private String lastName;
	private String email;
	private String dateOfBirth;
	private String phone;
	private String address;
	private String occupation;
	private String gender;
	private String password;
	private String membershipId;
	private LocalDate dateOfJoining;
	private LocalDate dateOfRenewal;
	private String deliveryArea;
	private String paymentMode;
	private String genre;
	private String instruction;
	private String role;
	private LocalDateTime createdAt;
	
	@ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="subscriptionId")
	private Subscription subscription;
	
	@OneToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL) @JoinColumn(name="parentId", referencedColumnName = "id", nullable=true)
	private Parent parent;
	
	@ManyToMany
	@JoinTable(name = "reading_queue", joinColumns = @JoinColumn(name = "userId"), inverseJoinColumns = @JoinColumn(name = "bookId"))
	private List<Books> readingQueue;
	
	@ManyToMany
	@JoinTable(name = "wishlist", joinColumns = @JoinColumn(name = "userId"), inverseJoinColumns = @JoinColumn(name = "bookId"))
	private List<Books> wishlist;
	
	@OneToMany(mappedBy="user")
	private List<UserHistory> userHistory;
	
	/*@OneToMany(mappedBy="user")
	private List<Blog> blogs;*/
	
	@ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="statusId")
	private UserStatus status;
	
	public String getFullName() {
		return getFirstName() + ' ' + getMiddleName() + ' ' + getLastName();
	}
}
