package com.incentives.piggyback.notification.serviceimpl;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import com.incentives.piggyback.notification.entity.OfferEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.incentives.piggyback.notification.entity.BroadcastRequest;
import com.incentives.piggyback.notification.entity.EmailRequest;
import com.incentives.piggyback.notification.entity.InvoiceRequest;
import com.incentives.piggyback.notification.publisher.NotificationEventPublisher;
import com.incentives.piggyback.notification.service.NotificationService;
import com.incentives.piggyback.notification.utils.CommonUtility;
import com.incentives.piggyback.notification.utils.Constant;

import static org.apache.http.HttpHeaders.USER_AGENT;

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

	@Override
	public String emailInvoice(InvoiceRequest invoiceRequest) {
		String htmlContent = setInvoiceBodyContent(invoiceRequest);
		emailService.processSendMailOperation(invoiceRequest.getEmailId(),
				invoiceRequest.getSubject(), htmlContent);
		return null;
	}

	@Override
	public int webhookNotification(OfferEntity offer,String url) throws IOException {
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		String requestBody = offer.toString();
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(requestBody);
		wr.flush();
		wr.close();
		return con.getResponseCode();
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