package com.bookcentric.component.subscription;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.bookcentric.component.subscription.borrowlimit.BorrowLimit;
import com.bookcentric.component.subscription.category.Category;
import com.bookcentric.component.subscription.planduration.PlanDuration;
import com.bookcentric.component.subscription.subscriptionduration.SubscriptionDuration;

import lombok.Data;

@Entity
@Data
@Table(name="subscription")
public class Subscription {
	
	@Id @GeneratedValue
	private Integer id;	
	private Integer price;
	
	@ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="categoryId")
	private Category category;
	
	@ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="subscriptionDurationId")
	private SubscriptionDuration subscriptionDuration;
	
}
