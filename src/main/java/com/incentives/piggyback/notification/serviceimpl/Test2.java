package com.incentives.piggyback.notification.serviceimpl;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.incentives.piggyback.notification.entity.NotificationRequestModel;
import com.incentives.piggyback.notification.entity.PushNotificationHeader;
import com.incentives.piggyback.notification.entity.PushNotificationPayload;

public class Test2 {

	public static void main(String[] args) throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost postRequest = new HttpPost("https://fcm.googleapis.com/fcm/send");
		NotificationRequestModel notificationRequestModel = new NotificationRequestModel();
		PushNotificationHeader notificationData = new PushNotificationHeader();
		notificationData.setBody("Your exclusive voucher for Amazon is AMZ12");
		notificationData.setTitle("test2");
		notificationRequestModel.setNotification(notificationData);
		PushNotificationPayload payload = new PushNotificationPayload();
		payload.setBody("First Notification");
		payload.setPartner_url("www.xyz.com");
		payload.setTitle("Amazon voucher");
		payload.setVoucher_code("AS430");
		notificationRequestModel.setData(payload);
		notificationRequestModel.setTo("dMTu4uFMxTE:APA91bEZn7LS1mTwrlDUCf5pAGw1Q-wtYEDNMcltRh-5J07mI16UXEYEnBdNa9m3eGc0aHO2M3-qrNTlIqcHk_eAwjKAwQvUESwAfuKOrLRAXrNTSg7BHjTcFaisf6YWb0WtnuSKyPZg");
		Gson gson = new Gson();
		String json = gson.toJson(notificationRequestModel);
		StringEntity input = new StringEntity(json);
		input.setContentType("application/json");
		postRequest.addHeader("Authorization", "key=AAAARtF-GGU:APA91bEP0_1THYN0f4FRs3iSPVxUXqstkz9snLzDmtASATP4Vpfj6h0Lp6D_TbzfKFF1iP6lvarf97-BXPrJSu0Fj-6rPnTTzwQLSfa7ULO9swz9ew0XIxrEdIiXzo66kXZSHI-IBKkZ");
		postRequest.setEntity(input);
		HttpResponse response = httpClient.execute(postRequest);
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "+ response.getStatusLine().getStatusCode());
		} else if (response.getStatusLine().getStatusCode() == 200) {
			System.out.println("response:" + EntityUtils.toString(response.getEntity()));
		}
	}

}
