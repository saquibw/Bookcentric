package com.bookcentric.component.user;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.bookcentric.component.user.subscription.Subscription;

import lombok.Data;

@Entity
@Data
@Table(name="user")
public class User {
	
	@Id @GeneratedValue
	private Integer id;
	private String firstName;
	private String lastName;
	private Instant dateOfBirth;
	private String address;
	private Integer phone;
	private String email;
	private String password;
	private String membershipId;
	private Instant dateOfJoining;
	private Instant dateOfRenewal;
	
	@ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="subscriptionId", insertable=false, updatable=false)
	private Subscription subscription;
}
