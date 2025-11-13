package com.pet.todo_list_rest.service;

import com.pet.todo_list_rest.dto.TaskCreateRequestDto;
import com.pet.todo_list_rest.dto.TaskPatchStatusRequestDto;
import com.pet.todo_list_rest.dto.TaskPutRequestDto;
import com.pet.todo_list_rest.dto.TaskResponseDto;
import com.pet.todo_list_rest.entity.Task;
import com.pet.todo_list_rest.exception.TaskNotFoundException;
import com.pet.todo_list_rest.mapper.TaskMapper;
import com.pet.todo_list_rest.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Transactional
    public TaskResponseDto createTask(TaskCreateRequestDto request) {
        Task entity = taskMapper.toEntity(request);
        Task savedTask = taskRepository.save(entity);
        return taskMapper.toResponseDto(savedTask);
    }

    public Page<Task> getTasksPaginated(int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);

        Pageable pageable = PageRequest.of(page, size, sort);

        return taskRepository.findAll(pageable);
    }

    public List<Task> getTasksWithStatus(Task.TaskStatus status) {
        return taskRepository.findByStatus(status);
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }


    @Transactional
    public TaskResponseDto updateTask(Long id, TaskPutRequestDto request) {
        Task existing = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        taskMapper.updateEntityFromPutDto(request, existing);
        Task saved = taskRepository.save(existing);
        return taskMapper.toResponseDto(saved);
    }

    @Transactional
    public TaskResponseDto updateTaskStatus(Long id, TaskPatchStatusRequestDto request) {
        Task existing = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        taskMapper.updateEntityFromPatchStatusDto(request, existing);
        Task saved = taskRepository.save(existing);
        return taskMapper.toResponseDto(saved);
    }

    @Transactional
    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        taskRepository.delete(task);
    }
}
