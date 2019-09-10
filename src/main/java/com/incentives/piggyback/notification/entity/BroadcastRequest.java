package com.incentives.piggyback.notification.entity;

import java.util.List;

import lombok.Data;

@Data
public class BroadcastRequest {

	private PushNotificationRequest pushNotificationRequest;
	private List<EmailRequest> emailRequestList;
}