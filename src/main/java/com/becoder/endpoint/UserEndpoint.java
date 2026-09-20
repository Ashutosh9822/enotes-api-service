package com.becoder.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.becoder.dto.PasswordChngRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User",description = "Authentication user Operation APIs")
@RequestMapping("/api/v1/user")
public interface UserEndpoint {

	@Operation(summary = "Get user profile",description = "Get user profile")
	@GetMapping("/profile")
	public ResponseEntity<?> getProfile();
	
	@Operation(summary = "User account password change",description = "User account password change")
	@PostMapping("/change-password")
	public ResponseEntity<?> changePassword(@RequestBody PasswordChngRequest passwordChngRequest);
	
}
