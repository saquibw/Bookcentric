package com.bookcentric.component.giftsubscription;

import lombok.Data;

@Data
public class GiftSubscription {

	private String giver;
	private String recipient;
	private String email;
	private String phone;
	private Double amount;
	private String address;
	private String deliveryDate;
	private Integer certificateDesign;
	private Integer option;
	private String message;
}
