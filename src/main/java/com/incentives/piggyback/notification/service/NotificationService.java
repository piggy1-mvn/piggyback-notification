package com.incentives.piggyback.notification.service;

import com.incentives.piggyback.notification.entity.BroadcastRequest;
import com.incentives.piggyback.notification.exception.PiggyException;

public interface NotificationService {

	String broadcastNotification(BroadcastRequest broadcastRequest) throws PiggyException;

}