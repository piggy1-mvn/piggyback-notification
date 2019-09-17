package com.incentives.piggyback.notification.controller;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.incentives.piggyback.notification.entity.BroadcastRequest;
import com.incentives.piggyback.notification.entity.InvoiceRequest;
import com.incentives.piggyback.notification.exception.PiggyException;
import com.incentives.piggyback.notification.service.NotificationService;
import com.incentives.piggyback.notification.utils.RestResponse;
import com.incentives.piggyback.notification.utils.RestUtils;

import javax.net.ssl.HttpsURLConnection;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.apache.http.HttpHeaders.USER_AGENT;

@RestController
@RequestMapping(value="/notification")
public class NotificationController {

	@Autowired
	private NotificationService notificationService;

    @PostMapping("/broadcast")
	public ResponseEntity<RestResponse<String>> broadcastNotification(@RequestBody 
			BroadcastRequest broadcastRequest) throws PiggyException {
		return RestUtils.successResponse(notificationService.broadcastNotification(broadcastRequest));
	}

	@PostMapping("/webhook")
	public ResponseEntity<RestResponse<String>> webhookNotification(@RequestParam(name = "webhookurl") String url,
	@RequestParam(name="notificationId") String notificationId, @RequestParam(name="notificationTitle") String notificationTitle,
																	@RequestParam(name="notificationMessage") String notificationMessage) throws IOException {
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		String urlParameters = "notificationId=" + notificationId + "&notificationTitle=" + notificationTitle + "&notificationMessage=" + notificationMessage ;
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		return ResponseEntity.status(con.getResponseCode()).build();
	}
    
  @PostMapping("/invoice")
	public ResponseEntity<RestResponse<String>> emailInvoice(@RequestBody 
			InvoiceRequest invoiceRequest) throws PiggyException {
		return RestUtils.successResponse(notificationService.emailInvoice(invoiceRequest));
	}
 
}
