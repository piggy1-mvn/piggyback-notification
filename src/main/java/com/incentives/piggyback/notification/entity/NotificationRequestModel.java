package com.incentives.piggyback.notification.entity;

import lombok.Data;

@Data
public class NotificationRequestModel {

    private PushNotificationHeader notification;
    private String to;
    private String priority = "high";
    private PushNotificationPayload data;
}