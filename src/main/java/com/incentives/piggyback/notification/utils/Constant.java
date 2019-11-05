package com.incentives.piggyback.notification.utils;

public interface Constant {
	
	Integer SUCCESS_STATUS = 200;
	Integer FAILURE_STATUS = 101;
    String NOTIFICATION_PUBLISHER_TOPIC = "notification.publish.topic";
    String NOTIFICATION_SOURCE_ID = "4";
    String NOTIFICATION_CREATED_EVENT = "Notification Events Created";
    String NOTIFICATION_UPDATED_EVENT = "Notification Events Updated";
    String NOTIFICATION_DEACTIVATED_EVENT = "Notification Events Deactivated";

    String KAFKA_BOOTSTRAP_ADDRESS = "kafka.bootstrap.address";
    
    public interface Email {
    	String COUPON_CODE = "{COUPON_CODE}";
    	String VENDOR = "{vendor}";
    	String LINK = "{unique_link}";
    	String AMOUNT = "{amount}";
    }
    
    public interface Environment {
    	String EMAIL_HTML_CONTENT = "html.coupon.email";
    	String EMAIL_HTML_INVOICE = "html.invoice.email";
    }
}