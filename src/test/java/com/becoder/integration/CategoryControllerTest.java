package com.becoder.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.becoder.dto.CategoryDto;
import com.becoder.dto.LoginRequest;
import com.becoder.entity.Category;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@ActiveProfiles("dev")
@AutoConfigureMockMvc
public class CategoryControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private CategoryDto categoryDto;
	private Category category;

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
	}

	@Test
	public void testSaveCategory() throws Exception {

		String token = generateToken("ashutoshbeura636@gmail.com", "1211");

		mockMvc.perform(post("/api/v1/category/save-category").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(categoryDto)).header("Authorization", token))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.message").value("saved success"))
				.andExpect(jsonPath("$.status").value("success"));
	}

//	public String generateToken(String email, String password) throws Exception {
//
//		LoginRequest login = new LoginRequest();
//		login.setEmail(email);
//		login.setPassword(password);
//
//		String response = mockMvc
//				.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON)
//						.content(objectMapper.writeValueAsString(login)))
//				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
//
//		JsonNode root = objectMapper.readTree(response);
//		String token = root.path("data").path("token").asText();
//
//		return "Bearer " + token;
//	}
	
	public String generateToken(String email, String password) throws Exception {

	    LoginRequest login = new LoginRequest();
	    login.setEmail(email);
	    login.setPassword(password);

	    var result = mockMvc.perform(
	            post("/api/v1/auth/login")
	                    .contentType(MediaType.APPLICATION_JSON)
	                    .content(objectMapper.writeValueAsString(login))
	    ).andReturn();

	    System.out.println("====================================");
	    System.out.println("LOGIN STATUS: " + result.getResponse().getStatus());
	    System.out.println("LOGIN RESPONSE: "
	            + result.getResponse().getContentAsString());
	    System.out.println("====================================");

	    JsonNode root = objectMapper.readTree(
	            result.getResponse().getContentAsString()
	    );

	    String token = root.path("data").path("token").asText();

	    System.out.println("TOKEN = " + token);

	    return "Bearer " + token;
	}
	
}