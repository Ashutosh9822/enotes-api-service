package com.becoder.dto;

public class EmailRequest {
	
	private String to;
	
	private String subject;
	
	private String title;
	
	private String message;

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "EmailRequest [to=" + to + ", subject=" + subject + ", title=" + title + ", message=" + message + "]";
	}
	
	
}
