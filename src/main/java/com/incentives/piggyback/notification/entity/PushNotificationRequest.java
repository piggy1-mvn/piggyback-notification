package com.incentives.piggyback.notification.entity;

import java.util.List;

import lombok.Data;

@Data
public class PushNotificationRequest {

	private PushNotificationPayload pushNotificationPayload;
	private List<String> recepients;
}