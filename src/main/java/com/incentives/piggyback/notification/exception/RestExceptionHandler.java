package com.incentives.piggyback.notification.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.incentives.piggyback.notification.utils.CommonUtility;
import com.incentives.piggyback.notification.utils.RestUtils;

import java.io.IOException;
import java.net.MalformedURLException;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
	private static final Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);

	@ExceptionHandler(value = {Exception.class})
	protected ResponseEntity<?> handleUnknownException(Exception ex, WebRequest request) {
		log.error(ex.getMessage(), ex);
		return RestUtils.errorResponseEntity(ExceptionResponseCode.GENRAL_ERROR.getDescription(),
				PiggyException.DEFAULT_HTTP_STATUS);
	}

	@ExceptionHandler(value = {PiggyException.class})
	protected ResponseEntity<?> handleknownException(PiggyException ex, WebRequest request) {
		if(!CommonUtility.isNullObject(ex.getResponseCode())){
			log.error("Custom Exception:: Error Code :: {} Custom Exception:: Error Description {}",ex.getResponseCode().getCode(), ex.getResponseCode().getDescription());
		}else{
			log.error("General Exception:: Error Description {} ",ex.getMessage());
		}
		return RestUtils.errorResponseData((CommonUtility.isNullObject(ex.getResponseCode())?
				ExceptionResponseCode.GENRAL_ERROR : ex.getResponseCode()), HttpStatus.OK,ex.getMessage());
	}

	@ExceptionHandler(value = {IOException.class})
	protected ResponseEntity<?> handleMalformedURLException(IOException ex) {
		log.error(ex.getMessage(), ex);
		return RestUtils.errorResponseEntity(ExceptionResponseCode.GENRAL_ERROR.getDescription(),
				PiggyException.DEFAULT_HTTP_STATUS);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		return new ResponseEntity<>(ExceptionResponseCode.GENRAL_ERROR.getDescription(), HttpStatus.BAD_REQUEST);
	}
}