package com.bookcentric.component.user.paymentmode;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="paymentMode")
public class PaymentMode {
	
	@Id @GeneratedValue
	private Integer id;
	private String name;

}
