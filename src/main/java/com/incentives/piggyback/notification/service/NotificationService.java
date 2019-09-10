package com.incentives.piggyback.notification.service;

import com.incentives.piggyback.notification.entity.BroadcastRequest;

public interface NotificationService {

	String broadcastNotification(BroadcastRequest broadcastRequest);

}