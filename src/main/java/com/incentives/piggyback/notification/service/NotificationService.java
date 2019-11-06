package com.incentives.piggyback.notification.service;

import com.incentives.piggyback.notification.entity.BroadcastRequest;
import com.incentives.piggyback.notification.entity.InvoiceRequest;
import com.incentives.piggyback.notification.entity.OfferEntity;

import java.io.IOException;
import java.security.PublicKey;

public interface NotificationService {

	String broadcastNotification(BroadcastRequest broadcastRequest);

	String emailInvoice(InvoiceRequest invoiceRequest);

	int webhookNotification(OfferEntity offer, String url) throws IOException;

}