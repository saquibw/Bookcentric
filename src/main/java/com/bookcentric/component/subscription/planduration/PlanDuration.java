package com.bookcentric.component.subscription.planduration;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name="plan_duration")
public class PlanDuration {
	
	@Id @GeneratedValue
	private Integer id;
	private String name;
	private Integer durationInDays;
}
