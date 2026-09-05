package com.becoder.dto;

public class LoginResponse {

	private UserRequest user;
	
	private String token;

	public UserRequest getUser() {
		return user;
	}

	public void setUser(UserRequest user) {
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
