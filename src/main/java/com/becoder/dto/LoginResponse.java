package com.becoder.dto;

public class LoginResponse {

	private UserDto user;
	
	private String token;

	public UserDto getUser() {
		return user;
	}

	public void setUser(UserDto user) {
		this.user = user;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "LoginResponse [user=" + user + ", token=" + token + "]";
	}
	
}
