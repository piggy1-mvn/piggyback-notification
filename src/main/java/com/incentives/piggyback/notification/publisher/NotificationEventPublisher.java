package com.incentives.piggyback.notification.publisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.cloud.gcp.pubsub.integration.outbound.PubSubMessageHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.MessageHandler;
import org.springframework.stereotype.Service;

import com.incentives.piggyback.notification.utils.Constant;

@Service
public class NotificationEventPublisher {

	@Autowired
	private Environment env;

	@Bean
	@ServiceActivator(inputChannel = "pubsubOutputChannel")
	public MessageHandler messageSender(PubSubTemplate pubsubTemplate) {
		return new PubSubMessageHandler(pubsubTemplate, env.getProperty(Constant.NOTIFICATION_PUBLISHER_TOPIC));
	}

	@MessagingGateway(defaultRequestChannel = "pubsubOutputChannel")
	public interface PubsubOutboundGateway {
		void sendToPubsub(String message);
	}
}