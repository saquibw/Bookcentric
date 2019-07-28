package com.bookcentric.component.subscription;

import lombok.Data;

@Data
public class SubscriptionResponse {

	private String name;
	private Integer limitEachTime;
	private Integer limitPerMonth;
	private String duration;
	private Integer securityAmount;
	private Integer monthlyAmount;
	private Integer halfYearlyAmount;
	private Integer yearlyAmount;
	private boolean cancellationPolicy;
	
}
