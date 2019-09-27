package com.incentives.piggyback.notification.serviceimpl;

import java.io.IOException;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.incentives.piggyback.notification.entity.BroadcastRequest;
import com.incentives.piggyback.notification.entity.EmailRequest;
import com.incentives.piggyback.notification.entity.InvoiceRequest;
import com.incentives.piggyback.notification.entity.OfferEntity;
import com.incentives.piggyback.notification.publisher.NotificationEventPublisher;
import com.incentives.piggyback.notification.service.NotificationService;
import com.incentives.piggyback.notification.utils.CommonUtility;
import com.incentives.piggyback.notification.utils.Constant;

@Service
public class NotificationServiceImpl implements NotificationService {

	@Autowired
	private EmailServiceImpl emailService;

	@Autowired 
	private PushNotificationAdapter notificationAdapter;

	@Autowired
	private Environment environment;

	private static final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

	@Autowired
	private NotificationEventPublisher.PubsubOutboundGateway messagingGateway;

	Gson gson = new Gson();

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public String broadcastNotification(BroadcastRequest broadcastRequest) {
		if (CommonUtility.isValidList(broadcastRequest.getEmailRequestList())) {
			for (EmailRequest emailRequest : broadcastRequest.getEmailRequestList()) {
				String htmlContent = setBodyContent(emailRequest);
				emailService.processSendMailOperation(emailRequest.getEmailId(),
						emailRequest.getSubject(), htmlContent);
			}
		}
		if (! CommonUtility.isNullObject(broadcastRequest.getPushNotificationRequest())) {
			notificationAdapter.sendAndroidNotification(broadcastRequest.getPushNotificationRequest().getRecepients(),
					broadcastRequest.getPushNotificationRequest().getPushNotificationPayload());
		}
		messagingGateway.sendToPubsub(
				CommonUtility.stringifyEventForPublish(
						gson.toJson(broadcastRequest),
						Constant.NOTIFICATION_CREATED_EVENT,
						Calendar.getInstance().getTime().toString(),
						"",
						Constant.NOTIFICATION_SOURCE_ID
						));

		return "Broadcasted Successfully!";
	}

	@Override
	public String emailInvoice(InvoiceRequest invoiceRequest) {
		String htmlContent = setInvoiceBodyContent(invoiceRequest);
		emailService.processSendMailOperation(invoiceRequest.getEmailId(),
				invoiceRequest.getSubject(), htmlContent);
		return null;
	}

	@Override
	public int webhookNotification(OfferEntity offer,String url) throws IOException {
		log.info("webhookNotification offer {} url {}",offer,url);
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
		HttpEntity<?> entity = new HttpEntity<>(offer, headers);
		restTemplate.postForLocation(url, entity);
		return 200;
	}


	private String setInvoiceBodyContent(InvoiceRequest invoiceRequest) {
		String htmlContent = environment.getProperty(Constant.Environment.EMAIL_HTML_INVOICE)
				.replace(Constant.Email.VENDOR, invoiceRequest.getVendorDisplayName())
				.replace(Constant.Email.AMOUNT, invoiceRequest.getTotalAmount());
		return htmlContent;
	}

	private String setBodyContent(EmailRequest emailRequest) {
		String htmlContent = environment.getProperty(Constant.Environment.EMAIL_HTML_CONTENT)
				.replace(Constant.Email.COUPON_CODE, emailRequest.getCouponCode())
				.replace(Constant.Email.VENDOR, emailRequest.getVendorDisplayName())
				.replace(Constant.Email.LINK, emailRequest.getRedirectUrl());
		return htmlContent;
	}


}