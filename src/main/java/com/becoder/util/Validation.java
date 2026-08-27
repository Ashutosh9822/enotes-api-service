package com.becoder.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.becoder.dto.CategoryDto;
import com.becoder.dto.TodoDto;
import com.becoder.dto.TodoDto.StatusDto;
import com.becoder.dto.UserDto;
import com.becoder.enums.TodoStatus;
import com.becoder.exception.ResourceNotFoundException;
import com.becoder.exception.ValidationException;
import com.becoder.repository.RoleRepository;

import org.springframework.util.StringUtils;

@Component
public class Validation {

	@Autowired
	private RoleRepository roleRepository;

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

		if (!error.isEmpty()) {
			throw new ValidationException(error);
		}

	}

	public void todoValidation(TodoDto todo) throws Exception {
		StatusDto reqstatus = todo.getStatus();
		Boolean statusFound = false;
		for (TodoStatus st : TodoStatus.values()) {
			if (st.getId().equals(reqstatus.getId())) {
				statusFound = true;
			}
		}
		if (!statusFound) {
			throw new ResourceNotFoundException("Invalid status");
		}
	}

	public void userValidation(UserDto userDto) {
		if (!StringUtils.hasText(userDto.getFirstName())) {
			throw new IllegalArgumentException("First Name is Invalid");
		}

		if (!StringUtils.hasText(userDto.getLastName())) {
			throw new IllegalArgumentException("Last Name is Invalid");
		}

		if (!StringUtils.hasText(userDto.getEmail()) || !userDto.getEmail().matches(Constants.EMAIL_REGEX)) {
			throw new IllegalArgumentException("Email is Invalid");
		}

		if (!StringUtils.hasText(userDto.getMobNo()) || !userDto.getMobNo().matches(Constants.MOBNO_REGEX)) {
			throw new IllegalArgumentException("Mobile no. is Invalid");
		}
		
		if (CollectionUtils.isEmpty(userDto.getRoles())) {
			throw new IllegalArgumentException("Role is Invalid");
		} else {
			List<Integer> roleIds = roleRepository.findAll().stream().map(r -> r.getId()).toList();
			List<Integer> invalidReqRoleids = userDto.getRoles().stream().map(r -> r.getId()).filter(roleId -> !roleIds.contains(roleId)).toList();

			if (!CollectionUtils.isEmpty(invalidReqRoleids)) {
				throw new IllegalArgumentException("Role is Invalid" + invalidReqRoleids);
			}
		}
		
	}

}
