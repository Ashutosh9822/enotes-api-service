package com.becoder.util;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.becoder.dto.CategoryDto;
import com.becoder.dto.TodoDto;
import com.becoder.dto.TodoDto.StatusDto;
import com.becoder.enums.TodoStatus;
import com.becoder.exception.ResourceNotFoundException;
import com.becoder.exception.ValidationException;

@Component
public class Validation {

	public void categoryValidation(CategoryDto categoryDto) {

		Map<String, Object> error = new LinkedHashMap<>();

		if (ObjectUtils.isEmpty(categoryDto)) {
			throw new IllegalArgumentException("category object/JSON should not be null ");
		} else {
			// Name filed Validation
			if (ObjectUtils.isEmpty(categoryDto.getName())) {
				error.put("name", "name field is not empty or null");
			} else {
				if (categoryDto.getName().length() < 3) {
					error.put("name", "name length min 3");
				}
				if (categoryDto.getName().length() > 100) {
					error.put("name", "name length max 100");
				}
			}

			// Description filed Validation
			if (ObjectUtils.isEmpty(categoryDto.getDescription())) {
				error.put("description", "description field is not empty or null");
			}

			// isActive filed Validation
			if (ObjectUtils.isEmpty(categoryDto.getIsActive())) {
				error.put("isActive", "IsActive field is not empty or null");
			} else {
				if (categoryDto.getIsActive() != Boolean.TRUE && categoryDto.getIsActive() != Boolean.FALSE) {
					error.put("isActive", "invalid value for isActive field");
				}
			}	
		}
		
		if(!error.isEmpty()) {
			throw new ValidationException(error);
		}
		
	}
	
	public void todoValidation(TodoDto todo) throws Exception {
		StatusDto reqstatus = todo.getStatus();
		Boolean statusFound=false;
		for(TodoStatus st : TodoStatus.values()) {
			if(st.getId().equals(reqstatus.getId())) {
				statusFound=true;
			}
		}
		if(!statusFound) {
			throw new ResourceNotFoundException("Invalid status");
		}
	}
	

}
