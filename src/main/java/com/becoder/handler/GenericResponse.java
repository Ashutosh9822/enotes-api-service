package com.becoder.handler;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;

import lombok.Builder;

@Builder
public class GenericResponse {
	
	private HttpStatus responseStatus;
	
	private String status;
	
	private String message;
	
	private Object data;

	public HttpStatus getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(HttpStatus responseStatus) {
		this.responseStatus = responseStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "GenericResponse [responseStatus=" + responseStatus + ", status=" + status + ", message=" + message
				+ ", data=" + data + "]";
	}
	
	public ResponseEntity<?> create(){
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("status", status);
		map.put("message", message);
		
		if(!ObjectUtils.isEmpty(data)) {
			map.put("data", data);
		}
		return new ResponseEntity<> (map,responseStatus);
	}
	
}
