package com.becoder.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PasswordChngRequest {

	@NotBlank(message = "Old password is required")
	private String oldPassword;
	
	@NotBlank(message = "New password is required")
	@Size(min = 4, message = "New password must contain at least 4 characters")
	private String newPassword;

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	@Override
	public String toString() {
		return "PasswordChngRequest [oldPassword=" + oldPassword + ", newPassword=" + newPassword + "]";
	}
	
}
