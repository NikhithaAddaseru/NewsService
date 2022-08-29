//package com.newsservice.application.exception;
//
//import java.util.NoSuchElementException;
//
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.HttpRequestMethodNotSupportedException;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.context.request.WebRequest;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
//
////To handle exceptions globally
//@ControllerAdvice 
//public class GlobalExceptionHandling extends ResponseEntityExceptionHandler{
//
//	
//	@ExceptionHandler(NoSuchElementException.class)
//	public ResponseEntity<String>handleNoSuchElementException(NoSuchElementException noSuchElementException){
//		return new ResponseEntity<String>("No Value is Present in DB, please verify your request",HttpStatus.NOT_FOUND);//Response code as 404
//	}
//	
//	@Override
//	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
//			HttpHeaders headers, HttpStatus status, WebRequest request) {
//		// TODO Auto-generated method stub
//		return new ResponseEntity<Object>("Please change Method Request Type",HttpStatus.METHOD_NOT_ALLOWED);//Response code as 405
//	}
//}
