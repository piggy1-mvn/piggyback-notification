package com.incentives.piggyback.notification.serviceimpl;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.incentives.piggyback.notification.entity.NotificationRequestModel;
import com.incentives.piggyback.notification.entity.PushNotificationHeader;
import com.incentives.piggyback.notification.entity.PushNotificationPayload;
import com.incentives.piggyback.notification.exception.ExceptionResponseCode;
import com.incentives.piggyback.notification.exception.PiggyException;
import com.incentives.piggyback.notification.utils.CommonUtility;

@Component
public class PushNotificationAdapter {

	@Autowired
	protected Environment environment;

	private static final Logger log = LoggerFactory.getLogger(PushNotificationAdapter.class);


	Gson gson = new Gson();

	public void sendAndroidNotification(final List<String> recepients, final PushNotificationPayload payload) {

		final String FCM_SERVER_KEY = environment.getProperty("notification.fcm.server.key");
		final String FCM_SERVER_URL = environment.getProperty("notification.fcm.server.url");
		if (!(CommonUtility.isValidList(recepients) && CommonUtility.isValidString(FCM_SERVER_KEY)))
			throw new PiggyException(ExceptionResponseCode.USER_DATA_NOT_FOUND_IN_REQUEST);

		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost postRequest = new HttpPost(FCM_SERVER_URL);
		NotificationRequestModel notificationRequestModel = new NotificationRequestModel();
		PushNotificationHeader notificationData = new PushNotificationHeader();
		notificationData.setBody(payload.getBody());
		notificationData.setTitle(payload.getTitle());
		notificationRequestModel.setNotification(notificationData);
		notificationRequestModel.setData(payload);
		recepients.forEach(recepient -> {
			try {
				notificationRequestModel.setTo(recepient);
				String json = gson.toJson(notificationRequestModel);
				StringEntity input = new StringEntity(json);
				input.setContentType("application/json");
				postRequest.addHeader("Authorization", "key="+FCM_SERVER_KEY);
				postRequest.setEntity(input);
				HttpResponse response = httpClient.execute(postRequest);
				if (response.getStatusLine().getStatusCode() != 200) {
					log.error("push notification sending failed to {} with status {}",recepient, response.getStatusLine().getStatusCode());
				}
			} catch (Exception e) {
				log.error("push notification sending failed to {} with error {}",recepient, e);
			}

		});
		try {
			httpClient.close();
		} catch (IOException e) {
			log.error("httpClient connection close failure with error {}", e);
		}
	}


}