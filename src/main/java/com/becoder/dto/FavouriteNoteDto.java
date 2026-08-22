package com.becoder.dto;

import lombok.Builder;

@Builder
public class FavouriteNoteDto {

	private Integer id;

	private NotesDto note;

	private Integer userId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public NotesDto getNote() {
		return note;
	}

	public void setNote(NotesDto note) {
		this.note = note;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "FavouriteNoteDto [id=" + id + ", note=" + note + ", userId=" + userId + "]";
	}
	

}
