package com.incentives.piggyback.notification.entity;

import java.util.List;

import lombok.Data;

@Data
public class PushNotificationRequest {

	private PushNotificationHeader pushNotificationPayload;
	private List<String> recepients;
}