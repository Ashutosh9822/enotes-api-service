package com.becoder.dto;

public class PswdResetRequest {

	private Integer uid;
	
	private String newPassword;

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	@Override
	public String toString() {
		return "PswdResetRequest [uid=" + uid + ", newPassword=" + newPassword + "]";
	}
	
}
