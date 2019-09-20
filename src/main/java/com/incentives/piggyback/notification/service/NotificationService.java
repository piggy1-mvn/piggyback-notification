package com.incentives.piggyback.notification.service;

import com.incentives.piggyback.notification.entity.BroadcastRequest;
import com.incentives.piggyback.notification.entity.InvoiceRequest;

public interface NotificationService {

	String broadcastNotification(BroadcastRequest broadcastRequest);

	String emailInvoice(InvoiceRequest invoiceRequest);

}