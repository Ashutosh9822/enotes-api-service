package com.becoder.exception;

import java.io.FileNotFoundException;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.becoder.util.CommonUtil;


@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> HandletException(Exception e){
//		return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		return CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(SuccessException.class)
	public ResponseEntity<?> HandletSuccessException(SuccessException e){
//		return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		return CommonUtil.createBuildResponseMessage(e.getMessage(), HttpStatus.OK);
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<?> HandleIllegalArgumentException(IllegalArgumentException e){
//		return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		return CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<?> HandleNullPointerEntityException(Exception e){
//		return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		return CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> HandleResourceNotFoundException(Exception e){
//		return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
		return CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<?> HandleValidationException(ValidationException e){
//		return new ResponseEntity<>(e.getErrors(),HttpStatus.BAD_REQUEST);
		return CommonUtil.createErrorResponse(e.getErrors(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(FileNotFoundException.class)
	public ResponseEntity<?> HandleFileNotFoundException(FileNotFoundException e){
//		return new ResponseEntity<>(e.getErrors(),HttpStatus.BAD_REQUEST);
		return CommonUtil.createErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ExistDataException.class)
	public ResponseEntity<?> HandleExistDataException(ExistDataException e){
		return CommonUtil.createErrorResponse(e.getMessage(),HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<?> HandleHttpMessageNotReadableException(HttpMessageNotReadableException e){
		return CommonUtil.createErrorResponse(e.getMessage(),HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<?> HandleHttpBadCredentialsException(BadCredentialsException e){
		return CommonUtil.createErrorResponse(e.getMessage(),HttpStatus.BAD_REQUEST);
	}
	
}
