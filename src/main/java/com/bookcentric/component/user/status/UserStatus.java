package com.bookcentric.component.user.status;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name="userStatus")
public class UserStatus {
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String name;
	
	// id = 1, status = Active
	// id = 2, status = Break
	// id = 3, status = Pending
	// id = 4, status = Canceled
	// id = 5, status Deleted
	

}
