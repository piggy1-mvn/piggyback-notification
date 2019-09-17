package com.incentives.piggyback.notification.serviceimpl;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.incentives.piggyback.notification.entity.NotificationRequestModel;
import com.incentives.piggyback.notification.entity.PushNotificationHeader;
import com.incentives.piggyback.notification.entity.PushNotificationPayload;


public class Test {

	public static void main(String[] args) throws ClientProtocolException, IOException {
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
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
		headers.set("Authorization", "key=AAAARtF-GGU:APA91bEP0_1THYN0f4FRs3iSPVxUXqstkz9snLzDmtASATP4Vpfj6h0Lp6D_TbzfKFF1iP6lvarf97-BXPrJSu0Fj-6rPnTTzwQLSfa7ULO9swz9ew0XIxrEdIiXzo66kXZSHI-IBKkZ");
		HttpEntity<?> entity = new HttpEntity<>(json, headers);
		String response1 = restTemplate.postForObject("https://fcm.googleapis.com/fcm/send",
				entity, String.class);
		System.out.println("response 1::"+ response1);
	}
}
