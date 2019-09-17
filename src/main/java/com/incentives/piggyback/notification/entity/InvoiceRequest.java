package com.incentives.piggyback.notification.entity;

import lombok.Data;

@Data
public class InvoiceRequest {
	
	private String emailId;
	private String vendorDisplayName;
	private String subject;
	private String totalAmount;
}