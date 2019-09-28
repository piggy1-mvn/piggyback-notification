package com.incentives.piggyback.notification;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.incentives.piggyback.notification.entity.BroadcastRequest;
import com.incentives.piggyback.notification.entity.EmailRequest;
import com.incentives.piggyback.notification.entity.InvoiceRequest;
import com.incentives.piggyback.notification.serviceimpl.NotificationServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { NotificationApplication.class })
public class NotificationApplicationTests {

	@Autowired
	private NotificationServiceImpl notificationService;

	@Test
	public void broadcastNotificationTest() {
		BroadcastRequest broadcastRequest = new BroadcastRequest();
		List<EmailRequest> emailRequestList = new ArrayList<EmailRequest>();
		EmailRequest emailRequest = new EmailRequest();
		emailRequest.setCouponCode("TEST123");
		emailRequest.setEmailId("piggyback.incentives@gmail.com");
		emailRequest.setSubject("Test Email");
		emailRequest.setRedirectUrl("https://amazon.com");
		emailRequest.setVendorDisplayName("Testing Email");
		emailRequestList.add(emailRequest);
		broadcastRequest.setEmailRequestList(emailRequestList);
		String testResponse = notificationService.broadcastNotification(broadcastRequest);
        Assert.assertNotNull(testResponse);
        Assert.assertEquals("Broadcasted Successfully!", testResponse);

	}

	public void emailInvoice() {
		InvoiceRequest invoiceRequest = new InvoiceRequest();
		invoiceRequest.setSubject("Test Invoice Email");
		invoiceRequest.setEmailId("piggyback.incentives@gmail.com");
		invoiceRequest.setTotalAmount("$768");
		invoiceRequest.setVendorDisplayName("Testing Email");		
		String testResponse = notificationService.emailInvoice(invoiceRequest);
		Assert.assertNotNull(testResponse);
        Assert.assertEquals("Invoice e-mailed Successfully!", testResponse);
	}
}