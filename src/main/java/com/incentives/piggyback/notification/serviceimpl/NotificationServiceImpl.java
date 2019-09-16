package com.incentives.piggyback.notification.serviceimpl;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.incentives.piggyback.notification.entity.BroadcastRequest;
import com.incentives.piggyback.notification.entity.EmailRequest;
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

	@Autowired
	private NotificationEventPublisher.PubsubOutboundGateway messagingGateway;

	Gson gson = new Gson();

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

	private String setBodyContent(EmailRequest emailRequest) {
		String htmlContent = environment.getProperty(Constant.Environment.EMAIL_HTML_CONTENT)
				.replace(Constant.Email.COUPON_CODE, emailRequest.getCouponCode())
				.replace(Constant.Email.VENDOR, emailRequest.getVendorDisplayName())
				.replace(Constant.Email.LINK, emailRequest.getRedirectUrl());
		return htmlContent;
	}
}