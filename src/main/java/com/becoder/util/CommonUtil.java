package com.becoder.util;

import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;

import com.becoder.entity.User;
import com.becoder.handler.GenericResponse;
import com.becoder.security.CustomUserDetails;

import jakarta.servlet.http.HttpServletRequest;

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

	public static String getContentType(String originalFileName) {
		String extension = FilenameUtils.getExtension(originalFileName);
		
		switch (extension) {
		case "pdf":
			return "application/pdf";
		case "xlsx":
			return "application/vnd.openxmlformats-officedocument.spreadsheettml.sheet";
		case "txt":
			return "text/plan";
		case "png":
			return "image/png";
		case "jpeg":
			return "image/jpeg";
		default:
			return "application/octet-stream";
		}
	}

	public static String getUrl(HttpServletRequest request) {
		String apiUrl=request.getRequestURL().toString();
		apiUrl= apiUrl.replace(request.getServletPath(), "");
		return apiUrl;
	}

	public static User getLoggedInUser() {
		try {
			CustomUserDetails logUser = (CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			return logUser.getUser();
		} catch (Exception e) {
			throw e;
		}
	}
	
}
