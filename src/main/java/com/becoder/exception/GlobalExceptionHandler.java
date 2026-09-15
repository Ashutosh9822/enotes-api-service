package com.becoder.exception;

import java.io.FileNotFoundException;
import java.nio.file.AccessDeniedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.becoder.util.CommonUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
	
	Logger log=LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> HandletException(Exception e){
		log.info("GlobalExceptionHandler : handletException() : {}",e.getMessage());
		return CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<?> HandleAccessDeniedException(AccessDeniedException e){
		log.info("GlobalExceptionHandler : handleAccessDeniedException() : {}",e.getMessage());
		return CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(SuccessException.class)
	public ResponseEntity<?> HandletSuccessException(SuccessException e){
		log.info("GlobalExceptionHandler : handletSuccessException() : {}",e.getMessage());
		return CommonUtil.createBuildResponseMessage(e.getMessage(), HttpStatus.OK);
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<?> HandleIllegalArgumentException(IllegalArgumentException e){
		log.info("GlobalExceptionHandler : handleIllegalArgumentException() : {}",e.getMessage());
		return CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<?> HandleNullPointerEntityException(Exception e){
		log.info("GlobalExceptionHandler : handleNullPointerEntityException() : {}",e.getMessage());
		return CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> HandleResourceNotFoundException(Exception e){
		log.info("GlobalExceptionHandler :  handleResourceNotFoundException() : {}",e.getMessage());
		return CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<?> HandleValidationException(ValidationException e){
		log.info("GlobalExceptionHandler : handleValidationException() : {}",e.getMessage());
		return CommonUtil.createErrorResponse(e.getErrors(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(FileNotFoundException.class)
	public ResponseEntity<?> HandleFileNotFoundException(FileNotFoundException e){
		log.info("GlobalExceptionHandler : handleFileNotFoundException() : {}",e.getMessage());
		return CommonUtil.createErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ExistDataException.class)
	public ResponseEntity<?> HandleExistDataException(ExistDataException e){
		log.info("GlobalExceptionHandler : handleExistDataException() : {}",e.getMessage());
		return CommonUtil.createErrorResponse(e.getMessage(),HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<?> HandleHttpMessageNotReadableException(HttpMessageNotReadableException e){
		log.info("GlobalExceptionHandler : handleHttpMessageNotReadableException() : {}",e.getMessage());
		return CommonUtil.createErrorResponse(e.getMessage(),HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<?> HandleHttpBadCredentialsException(BadCredentialsException e){
		log.info("GlobalExceptionHandler : handleHttpBadCredentialsException() : {}",e.getMessage());
		return CommonUtil.createErrorResponse(e.getMessage(),HttpStatus.BAD_REQUEST);
	}
	
}
