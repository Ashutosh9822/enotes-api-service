package com.becoder.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.becoder.handler.GenericResponse;

public class CommonUtil {

	public static ResponseEntity<?> createBuildResponse(Object data, HttpStatus status) {
		GenericResponse response = new GenericResponse();
		response.setResponseStatus(status);
		response.setStatus("success");
		response.setMessage("success");
		response.setData(data);
		return response.create();
	}
	
	public static ResponseEntity<?> createBuildResponseMessage(String message ,HttpStatus status) {
		GenericResponse response = new GenericResponse();
		response.setResponseStatus(status);
		response.setStatus("success");
		response.setMessage(message);
		return response.create();
	}
	
	public static ResponseEntity<?> createErrorResponse(Object data, HttpStatus status) {
		GenericResponse response = new GenericResponse();
		response.setResponseStatus(status);
		response.setStatus("falied");
		response.setMessage("falied");
		response.setData(data);
		return response.create();
	}
	
	public static ResponseEntity<?> createErrorResponseMessage(String message, HttpStatus status) {
		GenericResponse response = new GenericResponse();
		response.setResponseStatus(status);
		response.setStatus("failed");
		response.setMessage(message);
		return response.create();
	}

}
