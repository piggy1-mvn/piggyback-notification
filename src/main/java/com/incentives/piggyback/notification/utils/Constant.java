package com.incentives.piggyback.notification.utils;

public interface Constant {
	
	Integer SUCCESS_STATUS = 200;
	Integer FAILURE_STATUS = 101;
    String NOTIFICATION_PUBLISHER_TOPIC = "notificationEvents";
    String NOTIFICATION_SOURCE_ID = "4";
    String NOTIFICATION_CREATED_EVENT = "Notification Events Created";
    String NOTIFICATION_UPDATED_EVENT = "Notification Events Updated";
    String NOTIFICATION_DEACTIVATED_EVENT = "Notification Events Deactivated";
}
