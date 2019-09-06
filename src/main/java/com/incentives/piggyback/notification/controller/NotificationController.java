package com.incentives.piggyback.notification.controller;

import com.incentives.piggyback.notification.publisher.NotificationEventPublisher;
import com.incentives.piggyback.notification.util.CommonUtility;
import com.incentives.piggyback.notification.util.constants.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;

@RestController
public class NotificationController {

    @Autowired
    private NotificationEventPublisher.PubsubOutboundGateway messagingGateway;

    @PostMapping("/notifications")
    public void publishMessage(@RequestParam("message") String message) {
        //PUSHING MESSAGES TO GCP
        messagingGateway.sendToPubsub(
                CommonUtility.stringifyEventForPublish(
                        message,
                        Constant.NOTIFICATION_CREATED_EVENT,
                        Calendar.getInstance().getTime().toString(),
                        "",
                        Constant.NOTIFICATION_SOURCE_ID
                ));
    }
}
