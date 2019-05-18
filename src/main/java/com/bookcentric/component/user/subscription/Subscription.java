package com.bookcentric.component.user.subscription;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.bookcentric.component.user.category.Category;
import com.bookcentric.component.user.planduration.PlanDuration;

import lombok.Data;

@Entity
@Data
@Table(name="subscription")
public class Subscription {
	
	@Id @GeneratedValue
	private Integer id;
	private Integer securityAmount;
	private Integer price;
	private Boolean cancellationPolicy;
	
	@ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="categoryId", insertable=false, updatable=false)
	private Category category;
	
	@ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="planDurationId", insertable=false, updatable=false)
	private PlanDuration planDuration;
	
}
