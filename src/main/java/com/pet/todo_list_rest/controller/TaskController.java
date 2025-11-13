package com.pet.todo_list_rest.controller;

import com.pet.todo_list_rest.dto.*;
import com.pet.todo_list_rest.entity.Task;
import com.pet.todo_list_rest.exception.TaskNotFoundException;
import com.pet.todo_list_rest.mapper.TaskMapper;
import com.pet.todo_list_rest.service.TaskService;
import com.pet.todo_list_rest.utils.PaginationUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/tasks")
@Slf4j
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(@Valid @RequestBody TaskCreateRequestDto request) {
        log.info("Creating task with payload={}", request);

        TaskResponseDto createdTask = taskService.createTask(request);
        return ResponseEntity.ok(createdTask);
    }

    @GetMapping
    public ResponseEntity<PaginatedResponse<TaskResponseDto>> getTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "status") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        log.info("Getting paginated tasks page: {}, size: {}", page, size);

        Page<Task> taskPage = taskService.getTasksPaginated(page, size, sortBy, sortDirection);

        PaginatedResponse<TaskResponseDto> response = PaginationUtils.toPaginatedResponse(
                taskPage,
                taskMapper::toResponseDto
        );

        log.info("Retrieved {} tasks (page {}/{})",
                response.content().size(),
                page + 1, taskPage.getTotalPages());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getTask(@PathVariable Long id) {
        log.info("Getting task with id={}", id);

    return taskService.getTaskById(id)
            .map(taskMapper::toResponseDto)
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new TaskNotFoundException(id));
    }


    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> updateTask(@PathVariable Long id, @Valid @RequestBody TaskPutRequestDto request) {
        log.info("Updating task with id={} and payload={}", id, request);
        TaskResponseDto updatedTask = taskService.updateTask(id, request);
        return ResponseEntity.ok(updatedTask);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskResponseDto> updateTaskStatus(@PathVariable Long id, @Valid @RequestBody TaskPatchStatusRequestDto request) {
        log.info("Updating status for task with id={} and payload={}", id, request);
        TaskResponseDto updatedTask = taskService.updateTaskStatus(id, request);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericSuccessResponse> deleteTask(@PathVariable Long id) {
        log.info("Deleting task with id={}", id);
        taskService.deleteTask(id);
        return ResponseEntity.ok(new GenericSuccessResponse("Task deleted successfully"));
    }

    @GetMapping("/status")
    public ResponseEntity<List<TaskResponseDto>> getTasksWithStatus(
            @RequestParam Task.TaskStatus status) {
        log.info("Getting tasks with status: {}", status);

        List<Task> tasks = taskService.getTasksWithStatus(status);

        List<TaskResponseDto> response = tasks.stream()
                .map(taskMapper::toResponseDto)
                .toList();

        log.info("Retrieved {} tasks with status: {}",
                response.size(),
                status);

        return ResponseEntity.ok(response);
    }
}
