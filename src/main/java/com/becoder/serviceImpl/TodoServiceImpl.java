package com.becoder.serviceImpl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.becoder.dto.TodoDto;
import com.becoder.dto.TodoDto.StatusDto;
import com.becoder.entity.Todo;
import com.becoder.enums.TodoStatus;
import com.becoder.exception.ResourceNotFoundException;
import com.becoder.repository.TodoRepository;
import com.becoder.service.TodoService;
import com.becoder.util.Validation;

@Service
public class TodoServiceImpl implements TodoService {
	
	@Autowired
	private TodoRepository todoRepository;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private Validation validation;

	@Override
	public Boolean SaveTodo(TodoDto todoDto) throws Exception {
		validation.todoValidation(todoDto);
		
		Todo todo = mapper.map(todoDto, Todo.class);
		todo.setStatusId(todoDto.getStatus().getId());
		Todo saveTodo = todoRepository.save(todo);
		if(ObjectUtils.isEmpty(saveTodo)) {
			return false;
		}
		return true;
	}
	
	@Override
	public TodoDto getTodoById(Integer id) throws Exception {
		Todo todo = todoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Todo Not found! invalid ID"));
		TodoDto todoDto = mapper.map(todo, TodoDto.class);
		setStatus(todoDto,todo);	
		return todoDto;
	}

	private void setStatus(TodoDto todoDto, Todo todo) {
		for(TodoStatus st : TodoStatus.values()) {
			if(st.getId().equals(todo.getStatusId())) {
				StatusDto statusDto = new StatusDto();
				statusDto.setId(st.getId());
				statusDto.setName(st.getName());
				todoDto.setStatus(statusDto);
			}
		}
	}

	@Override
	public List<TodoDto> getTodoByUser() {
		Integer userId=1;
		List<Todo> todos = todoRepository.findByCreatedBy(userId);
		return todos.stream().map(td -> mapper.map(td, TodoDto.class)).toList();
	}
	

	
}
