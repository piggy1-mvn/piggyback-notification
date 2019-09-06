package com.incentives.piggyback.notification.serviceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.google.gson.Gson;
import com.incentives.piggyback.notification.entity.PushNotification;
import com.incentives.piggyback.notification.utils.CommonUtility;

@Component
public class NotificationAdapter {
	
	private final static Logger logger = LoggerFactory.getLogger(NotificationAdapter.class);

	@Autowired
	protected Environment environment;

	public void sendAndroidNotification(final String recepients, final PushNotification payload) {
		final String GOOGLE_SERVER_KEY = environment.getProperty("NOTIFICATION_GOOGLE_SERVER_KEY");
//		final String GSM_ENDPOINT = environment.getProperty("NOTIFICATION_GSM_ENDPOINT");
		if (CommonUtility.isValidString(recepients) && CommonUtility.isValidString(GOOGLE_SERVER_KEY)) {
			try {
				final String payLoadJson = new Gson().toJson(payload);
				final Sender sender = new Sender(GOOGLE_SERVER_KEY.trim());
				logger.debug("payLoadJson:: " + payLoadJson);
				final Message message = new Message.Builder().timeToLive(30).delayWhileIdle(true).addData("data", payLoadJson).build();
				final Result result = sender.send(message, recepients.trim(), 1);
				String res = result.getMessageId();
				if (res != null) {
					logger.debug(" pushNotification sent successfully to:: "+ result.getCanonicalRegistrationId());
				} else {
					logger.debug(" pushNotification was not sent to:: " + result.getCanonicalRegistrationId());
				}
				logger.debug("notifications sent, result:: " + result.getSuccess() + ", failure:: " + result.getFailure());
			} catch (Exception ioException) {
				logger.error("some exception occured while sending notifications ", ioException);
			}
		} else {
			logger.warn("No recepients for notification");
		}
	}

}