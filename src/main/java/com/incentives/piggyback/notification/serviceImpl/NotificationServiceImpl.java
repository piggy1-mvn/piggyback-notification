package com.incentives.piggyback.notification.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.incentives.piggyback.notification.entity.BroadcastRequest;
import com.incentives.piggyback.notification.entity.EmailRequest;
import com.incentives.piggyback.notification.exception.PiggyException;
import com.incentives.piggyback.notification.service.NotificationService;
import com.incentives.piggyback.notification.utils.CommonUtility;

@Service
public class NotificationServiceImpl implements NotificationService {
	
	@Autowired
	private EmailServiceImpl emailService;
	
	@Autowired 
	private PushNotificationAdapter notificationAdapter;
	
	@Autowired
	private Environment environment;

	@Override
	public String broadcastNotification(BroadcastRequest broadcastRequest) throws PiggyException {
		if (CommonUtility.isValidList(broadcastRequest.getEmailRequestList())) {
			
			for (EmailRequest emailRequest : broadcastRequest.getEmailRequestList()) {
				String htmlContent = setBodyContent(emailRequest);
				emailService.processSendMailOperation("info.piggyback@gmail.com", emailRequest.getEmailId(),
						emailRequest.getSubject(), htmlContent);
			}
		}
		
		if (! CommonUtility.isNullObject(broadcastRequest.getPushNotificationRequest())) {
			notificationAdapter.sendAndroidNotification(broadcastRequest.getPushNotificationRequest().getRecepients(),
					broadcastRequest.getPushNotificationRequest().getPushNotificationPayload());
		}
		
		return "Broadcasted Successfully!";
	}

	private String setBodyContent(EmailRequest emailRequest) {
		String htmlContent = environment.getProperty("html.coupon.email")
				.replace("{COUPON_CODE}", emailRequest.getCouponCode())
				.replace("{vendor}", emailRequest.getVendorDisplayName())
				.replace("{unique_link}", emailRequest.getRedirectUrl());
		return htmlContent;
	}
}