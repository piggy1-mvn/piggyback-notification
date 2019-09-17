package com.incentives.piggyback.notification.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incentives.piggyback.notification.entity.BroadcastRequest;
import com.incentives.piggyback.notification.exception.PiggyException;
import com.incentives.piggyback.notification.service.NotificationService;
import com.incentives.piggyback.notification.utils.RestResponse;
import com.incentives.piggyback.notification.utils.RestUtils;

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
}