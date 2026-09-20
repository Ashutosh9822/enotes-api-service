package com.becoder.endpoint;

import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static com.becoder.util.Constants.ROLE_USER;
import com.becoder.dto.TodoDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Todo",description = "All the Todo Operation APIs")
@RequestMapping("/api/v1/todo")
public interface TodoEndpoint {

	@Operation(summary = "Save Todo",description = "save todo")
	@PostMapping("/savetodo")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> saneTodo(@RequestBody TodoDto todo) throws Exception;
	
	@Operation(summary = "Get Todo",description = "Get Todo bt ID")
	@GetMapping("/Todo/{id}")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> getTodoById(@PathVariable Integer id) throws Exception;
	
	@Operation(summary = "Get All Todo By User",description = "get all todo by user")
	@GetMapping("/list")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> getAllTodoByUser();
	
}
