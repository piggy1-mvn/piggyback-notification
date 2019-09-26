package com.incentives.piggyback.notification.controller;

import com.incentives.piggyback.notification.entity.OfferEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.incentives.piggyback.notification.entity.BroadcastRequest;
import com.incentives.piggyback.notification.entity.InvoiceRequest;
import com.incentives.piggyback.notification.exception.PiggyException;
import com.incentives.piggyback.notification.service.NotificationService;
import com.incentives.piggyback.notification.utils.RestResponse;
import com.incentives.piggyback.notification.utils.RestUtils;
import java.io.IOException;


@RestController
@RequestMapping(value = "/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/broadcast")
    public ResponseEntity<RestResponse<String>> broadcastNotification(@RequestBody
                                                                              BroadcastRequest broadcastRequest) throws PiggyException {
        return RestUtils.successResponse(notificationService.broadcastNotification(broadcastRequest));
    }

    @PostMapping("/webhook")
    public ResponseEntity<RestResponse<Integer>> webhookNotification(@RequestBody OfferEntity offer, @RequestParam(name = "webhookurl") String url) throws IOException {
        return RestUtils.successResponse(notificationService.webhookNotification(offer, url));
    }

    @PostMapping("/invoice")
    public ResponseEntity<RestResponse<String>> emailInvoice(@RequestBody
                                                                     InvoiceRequest invoiceRequest) throws PiggyException {
        return RestUtils.successResponse(notificationService.emailInvoice(invoiceRequest));
    }

}
