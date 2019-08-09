package com.bookcentric.component.subscription.borrowlimit;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="borrow_limit")
public class BorrowLimit {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	Integer eachTime;
	Integer perMonth;
}
