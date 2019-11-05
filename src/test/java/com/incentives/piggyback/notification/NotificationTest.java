package com.incentives.piggyback.notification;

import static org.junit.Assert.assertEquals;

<<<<<<< HEAD
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
=======
>>>>>>> 611a2436d16bd03a396604141f9215d36384ef07
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.gson.Gson;
import com.incentives.piggyback.notification.controller.NotificationController;
import com.incentives.piggyback.notification.entity.BroadcastRequest;
import com.incentives.piggyback.notification.entity.EmailRequest;
import com.incentives.piggyback.notification.entity.InvoiceRequest;
import com.incentives.piggyback.notification.service.NotificationService;

<<<<<<< HEAD
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

@Slf4j
=======
>>>>>>> 611a2436d16bd03a396604141f9215d36384ef07
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class NotificationTest {

	private MockMvc mvc;

	@Mock
	private NotificationService notificationService;

	@InjectMocks
	NotificationController notificationController;

	@Before
	public void setUp() {
		mvc = MockMvcBuilders.standaloneSetup(notificationController).build();
	}

	@Test
	public final void broadcastContent() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
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
		Gson gson = new Gson();
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/notification/broadcast")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(gson.toJson(broadcastRequest));
		MvcResult result = mvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}
	
	@Test
	public void emailInvoice() throws Exception {
		InvoiceRequest invoiceRequest = new InvoiceRequest();
		invoiceRequest.setSubject("Test Invoice Email");
		invoiceRequest.setEmailId("piggyback.incentives@gmail.com");
		invoiceRequest.setTotalAmount("$768");
		invoiceRequest.setVendorDisplayName("Testing Email");		
		Gson gson = new Gson();
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/notification/invoice")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(gson.toJson(invoiceRequest));
		MvcResult result = mvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}
<<<<<<< HEAD
=======
//
//	@Test
//	public void keyGenerator() {
//		SecretKey secretKey = null;
//		try {
//			KeyGenerator keyGen = KeyGenerator.getInstance("AES");
//			keyGen.init(256); // for example
//			secretKey = keyGen.generateKey();
//			System.out.println("Secret key generation failed" + secretKey.getEncoded().toString());
//		}catch(NoSuchAlgorithmException e){
//			System.out.println("Secret key generation failed");
//		}
//	}
>>>>>>> 611a2436d16bd03a396604141f9215d36384ef07

}