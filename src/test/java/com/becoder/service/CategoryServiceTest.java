package com.becoder.service;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.modelmapper.ModelMapper;

import com.becoder.dto.CategoryDto;
import com.becoder.entity.Category;
import com.becoder.exception.ExistDataException;
import com.becoder.repository.CategoryRepository;
import com.becoder.serviceImpl.CategoryServiceImpl;
import com.becoder.util.Validation;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

	@Mock
	private CategoryRepository categoryRepository;

	@Mock
	private Validation validation;

	@Mock
	private ModelMapper mapper;

	@InjectMocks
	private CategoryServiceImpl categoryServiceImpl;

	private CategoryDto categoryDto;
	
	private Category category;
	
	private List<Category> categories=new ArrayList<>();
	
	private List<CategoryDto> categoriesDto=new ArrayList<>();

	@BeforeEach
	public void initialize() {

		categoryDto = new CategoryDto();

		categoryDto.setId(null);
		categoryDto.setName("Java Notes");
		categoryDto.setDescription("java notes");
		categoryDto.setIsActive(true);

		category = new Category();

		category.setId(null);
		category.setName("Java Notes");
		category.setDescription("java notes");
		category.setIsActive(true);
		category.setIsDeleted(false);
		
		categories.add(category);
		categoriesDto.add(categoryDto);
	}

	@Test
	public void testSaveCategory() {

		when(categoryRepository.existsByName(categoryDto.getName())).thenReturn(false);

		// Mapper is needed only in this test
		when(mapper.map(categoryDto, Category.class)).thenReturn(category);
		when(categoryRepository.save(category)).thenReturn(category);

		Boolean saveCategory = categoryServiceImpl.saveCategory(categoryDto);

		assertTrue(saveCategory);

		verify(validation).categoryValidation(categoryDto);
		verify(categoryRepository).existsByName(categoryDto.getName());
		verify(mapper).map(categoryDto, Category.class);
		verify(categoryRepository).save(category);
	}

	@Test
	public void testCategoryExists() {

		when(categoryRepository.existsByName(categoryDto.getName())).thenReturn(true);

		ExistDataException exception = assertThrows(ExistDataException.class,
				() -> categoryServiceImpl.saveCategory(categoryDto));

		assertEquals("Category already exist", exception.getMessage());

		verify(validation).categoryValidation(categoryDto);
		verify(categoryRepository).existsByName(categoryDto.getName());
		verify(categoryRepository, never()).save(category);
		verify(mapper, never()).map(categoryDto, Category.class);
	}

	@Test
	public void testUpdateCategory() {
		categoryDto.setId(1);
		category.setId(1);

		when(categoryRepository.existsByName(categoryDto.getName())).thenReturn(false);

		// Mapper is needed only in this test
		when(mapper.map(categoryDto, Category.class)).thenReturn(category);
		when(categoryRepository.save(category)).thenReturn(category);

		Boolean saveCategory = categoryServiceImpl.saveCategory(categoryDto);

		assertTrue(saveCategory);

		verify(validation).categoryValidation(categoryDto);
		verify(categoryRepository).existsByName(categoryDto.getName());
		verify(mapper).map(categoryDto, Category.class);
		verify(categoryRepository).save(category);
	}
	
	@Test
	public void testGetAllCategory() {
		when(categoryRepository.findByIsDeletedFalse()).thenReturn(categories);
		List<CategoryDto> allCategory = categoryServiceImpl.getAllCategory();
		assertEquals(allCategory.size(), categories.size());
		verify(categoryRepository).findByIsDeletedFalse();
	}
	
}