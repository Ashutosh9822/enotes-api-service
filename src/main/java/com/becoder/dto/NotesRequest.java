package com.becoder.dto;

import com.becoder.dto.NotesDto.CategoryDto;

public class NotesRequest {

	private String title;

	private String description;

	private CategoryDto category;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public CategoryDto getCategory() {
		return category;
	}

	public void setCategory(CategoryDto category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "NotesRequest [title=" + title + ", description=" + description + ", category=" + category + "]";
	}
	
}
