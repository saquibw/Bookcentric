package com.bookcentric.component.subscription.category;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.bookcentric.component.subscription.borrowlimit.BorrowLimit;
import com.bookcentric.component.subscription.planduration.PlanDuration;

import lombok.Data;

@Data
@Entity
@Table(name="category")
public class Category {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private Integer securityAmount;
	private boolean underAge;
	private boolean family;
	
	@ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="borrowLimitId")
	private BorrowLimit borrowLimit;
	
	@ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="planDurationId")
	private PlanDuration planDuration;
}
