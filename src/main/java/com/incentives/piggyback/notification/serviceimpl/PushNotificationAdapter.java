package com.incentives.piggyback.notification.serviceimpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;
import com.google.gson.Gson;
import com.incentives.piggyback.notification.entity.PushNotificationHeader;
import com.incentives.piggyback.notification.utils.CommonUtility;
@Component
public class PushNotificationAdapter {
	
	private static final Logger logger = LoggerFactory.getLogger(PushNotificationAdapter.class);

	@Autowired
	protected Environment environment;

	public void sendAndroidNotification(final List<String> recepients, final PushNotificationHeader payload) {
		final String GOOGLE_SERVER_KEY = environment.getProperty("NOTIFICATION_GOOGLE_SERVER_KEY");
		if (CommonUtility.isValidList(recepients) && CommonUtility.isValidString(GOOGLE_SERVER_KEY)) {
			try {
				final String payLoadJson = new Gson().toJson(payload);
				final Sender sender = new Sender(GOOGLE_SERVER_KEY.trim());
				logger.debug("payLoadJson:: {} ", payLoadJson);
				final Message message = new Message.Builder().timeToLive(30).delayWhileIdle(true).addData("data", payLoadJson).build();
				final MulticastResult result = sender.send(message, recepients, 1);
				logger.info("notifications sent result:: success count :: {} failure count :: {}", result.getSuccess(), result.getFailure());
			} catch (Exception ioException) {
				logger.error("some exception occured while sending notifications ", ioException);
			}
		} else {
			logger.warn("No recepients for notification");
		}
	}
	
	
}