package com.incentives.piggyback.notification.entity;

import lombok.Data;


import java.util.Date;
@Data
public class OfferEntity {


	private Long offerId;
	private String orderId;
	private String partnerId;
	private String partnerName;
	private String partnerAppUrl;
	private String offerCode;
	private Location orderLocation;
	private String orderType;
	private Long initiatorUserId;
	private Integer maxOptimizations;
	private String offerStatus;
	private String offerDescription;
	private Date createdDate;
	private Date lastModifiedDate;
	private Date expiryDate;
	private double optimizationRadius;
}