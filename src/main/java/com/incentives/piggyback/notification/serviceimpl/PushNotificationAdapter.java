package com.incentives.piggyback.notification.serviceimpl;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;

import com.google.android.gcm.server.InvalidRequestException;
import com.incentives.piggyback.notification.entity.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.PEMParser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.incentives.piggyback.notification.exception.ExceptionResponseCode;
import com.incentives.piggyback.notification.exception.PiggyException;
import com.incentives.piggyback.notification.utils.CommonUtility;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

@Component
public class PushNotificationAdapter {

	@Autowired
	protected Environment environment;

	private static final Logger log = LoggerFactory.getLogger(PushNotificationAdapter.class);


	Gson gson = new Gson();

	public void sendAndroidNotification(final List<ReceipientInfo> recepients, final PushNotificationPayload payload) {

		final String FCM_SERVER_KEY = environment.getProperty("notification.fcm.server.key");
		final String FCM_SERVER_URL = environment.getProperty("notification.fcm.server.url");
		if (!(CommonUtility.isValidList(recepients) && CommonUtility.isValidString(FCM_SERVER_KEY)))
			throw new PiggyException(ExceptionResponseCode.USER_DATA_NOT_FOUND_IN_REQUEST);

		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost postRequest = new HttpPost(FCM_SERVER_URL);
		recepients.forEach(recepient -> {
			NotificationRequestModel notificationRequestModel = new NotificationRequestModel();
			PushNotificationHeader notificationData = new PushNotificationHeader();
			PushNotificationNewPayload pushNotificationNewPayload = new PushNotificationNewPayload();
			notificationData.setBody(payload.getBody());
			notificationData.setTitle(payload.getTitle());
			notificationRequestModel.setNotification(notificationData);
			pushNotificationNewPayload.setBody(payload.getBody());
			pushNotificationNewPayload.setPartner_url(payload.getPartner_url());
			pushNotificationNewPayload.setTitle(payload.getTitle());

			try {
				pushNotificationNewPayload.setVoucher_code(getAesEncryptData(payload.getVoucher_code(), keyGenerator()));
				pushNotificationNewPayload.setKey(Base64.getEncoder().encodeToString(encryptAESkey(keyGenerator().getEncoded().toString(),recepient.getUser_rsa())));
			} catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException | IOException | InvalidKeySpecException e) {
				throw new PiggyException("Error while encryption of voucher code" + e);
			}
			notificationRequestModel.setData(pushNotificationNewPayload);

			try {
				notificationRequestModel.setTo(recepient.getDevice_id());
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

	private SecretKey keyGenerator() {
		SecretKey secretKey = null;
		try {
			KeyGenerator keyGen = KeyGenerator.getInstance("AES");
			keyGen.init(256); // for example
			secretKey = keyGen.generateKey();
		}catch(NoSuchAlgorithmException e){
			log.debug("Secret key generation failed");
		}
		log.info("Secret key generation is" + secretKey.getEncoded().toString());
		return secretKey;
	}

	private byte[] encryptAESkey(String data, String publicKey) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, IOException, InvalidKeySpecException {
		//Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, getRSAPublicKey(publicKey));
		byte[] cipherText = cipher.doFinal(data.getBytes());
		log.info("AES encypted key is " + cipherText.toString());
		return cipherText;
	}

	private static String getAesEncryptData(String plainText, SecretKey secretKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
		byte[] plaintext = plainText.getBytes();
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		SecretKeySpec keySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");
		cipher.init(Cipher.ENCRYPT_MODE, keySpec);
		byte[] cipherText = cipher.doFinal(plaintext);
		log.info("Encrypted Voucher code is "+ cipherText.toString() );
		return cipherText.toString();
	}

    private PublicKey getRSAPublicKey(String publicKey) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        Reader keyReader = null;
        try {
            keyReader = new StringReader(publicKey);
            PEMParser pemParser = new PEMParser(keyReader);
            SubjectPublicKeyInfo subjectPublicKeyInfo = (SubjectPublicKeyInfo) pemParser.readObject();
            X509EncodedKeySpec spec = new X509EncodedKeySpec(subjectPublicKeyInfo.getEncoded());
            return KeyFactory.getInstance("RSA").generatePublic(spec);
        } finally {
            if (keyReader != null) {
                keyReader.close();
            }
        }
    }





}