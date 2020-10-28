package com.ms.WebPlatform.exception;	

	import org.springframework.http.HttpStatus;
	import org.springframework.http.ResponseEntity;
	import org.springframework.web.bind.annotation.ControllerAdvice;
	import org.springframework.web.bind.annotation.ExceptionHandler;

	@ControllerAdvice
	public class ProductExceptionController {
	   @ExceptionHandler(value = ProductNotfoundException.class)
	   public ResponseEntity<Object> exception(ProductNotfoundException exception) {
	      return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
	   }
	   
	   @ExceptionHandler(value = CommonException.class)
	   public ResponseEntity<Object> exception(CommonException e) {
	      return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
	   }
	}


