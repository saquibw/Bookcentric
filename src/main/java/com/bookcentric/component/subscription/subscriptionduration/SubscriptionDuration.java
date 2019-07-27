package com.bookcentric.component.subscription.subscriptionduration;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name="subscription_duration")
public class SubscriptionDuration {
	
	@Id @GeneratedValue
	private Integer id;
	private String name;
	private Integer durationInDays;
}
