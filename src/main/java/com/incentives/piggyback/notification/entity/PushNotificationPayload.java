package com.incentives.piggyback.notification.entity;

import lombok.Data;

@Data
public class PushNotificationPayload {

	private String body;
	private String title;
	private String partner_url;
	private String voucher_code;
}