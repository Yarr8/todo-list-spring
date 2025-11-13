package com.pet.todo_list_rest;

import com.pet.todo_list_rest.dto.*;
import com.pet.todo_list_rest.entity.Task;
import com.pet.todo_list_rest.exception.TaskNotFoundException;
import com.pet.todo_list_rest.mapper.TaskMapper;
import com.pet.todo_list_rest.repository.TaskRepository;
import com.pet.todo_list_rest.service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    // --- createTask ---
    @Test
    void createTask_shouldMapAndSaveAndReturnResponseDto() {
        TaskCreateRequestDto request = mock();
        Task entity = mock();
        Task saved = mock();
        TaskResponseDto responseDto = mock();

        when(taskMapper.toEntity(request)).thenReturn(entity);
        when(taskRepository.save(entity)).thenReturn(saved);
        when(taskMapper.toResponseDto(saved)).thenReturn(responseDto);

        TaskResponseDto result = taskService.createTask(request);

        assertThat(result).isSameAs(responseDto);
        verify(taskMapper).toEntity(request);
        verify(taskRepository).save(entity);
        verify(taskMapper).toResponseDto(saved);
    }

    // --- getTasksPaginated ---
    @Test
    void getTasksPaginated_shouldReturnPageFromRepository() {
        int page = 0;
        int size = 5;
        String sortBy = "id";
        String sortDirection = "DESC";

        Pageable expectedPageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));
        Page<Task> mockPage = new PageImpl<>(List.of(new Task()));

        when(taskRepository.findAll(any(Pageable.class))).thenReturn(mockPage);

        Page<Task> result = taskService.getTasksPaginated(page, size, sortBy, sortDirection);

        assertThat(result).isSameAs(mockPage);
        verify(taskRepository).findAll((Pageable) argThat(argument -> {
                    Pageable pageable = (Pageable) argument;
                    return pageable.getPageNumber() == expectedPageable.getPageNumber()
                            && pageable.getPageSize() == expectedPageable.getPageSize()
                            && pageable.getSort().equals(expectedPageable.getSort());
                }
        ));
    }

    // --- getTaskById ---
    @Test
    void getTaskById_shouldReturnOptionalTask() {
        Long id = 1L;
        Task task = mock();
        when(taskRepository.findById(id)).thenReturn(Optional.of(task));

        Optional<Task> result = taskService.getTaskById(id);

        assertThat(result).contains(task);
        verify(taskRepository).findById(id);
    }

    // --- updateTask ---
    @Test
    void updateTask_shouldUpdateExistingTaskAndReturnResponseDto() {
        Long id = 1L;
        Task existing = mock();
        Task saved = mock();
        TaskPutRequestDto request = mock();
        TaskResponseDto responseDto = mock();

        when(taskRepository.findById(id)).thenReturn(Optional.of(existing));
        when(taskRepository.save(existing)).thenReturn(saved);
        when(taskMapper.toResponseDto(saved)).thenReturn(responseDto);

        TaskResponseDto result = taskService.updateTask(id, request);

        assertThat(result).isSameAs(responseDto);
        verify(taskMapper).updateEntityFromPutDto(request, existing);
        verify(taskRepository).save(existing);
    }

    @Test
    void updateTask_shouldThrowExceptionIfNotFound() {
        Long id = 1L;
        TaskPutRequestDto request = mock();

        when(taskRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.updateTask(id, request))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining(id.toString());
    }

    // --- updateTaskStatus ---
    @Test
    void updateTaskStatus_shouldUpdateStatusAndReturnResponseDto() {
        Long id = 1L;
        Task existing = mock();
        Task saved = mock();
        TaskPatchStatusRequestDto request = mock();
        TaskResponseDto responseDto = mock();

        when(taskRepository.findById(id)).thenReturn(Optional.of(existing));
        when(taskRepository.save(existing)).thenReturn(saved);
        when(taskMapper.toResponseDto(saved)).thenReturn(responseDto);

        TaskResponseDto result = taskService.updateTaskStatus(id, request);

        assertThat(result).isSameAs(responseDto);
        verify(taskMapper).updateEntityFromPatchStatusDto(request, existing);
        verify(taskRepository).save(existing);
    }

    @Test
    void updateTaskStatus_shouldThrowExceptionIfNotFound() {
        Long id = 1L;
        TaskPatchStatusRequestDto request = mock();

        when(taskRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.updateTaskStatus(id, request))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining(id.toString());
    }

    // --- deleteTask ---
    @Test
    void deleteTask_shouldDeleteIfExists() {
        Long id = 1L;
        Task existing = mock();

        when(taskRepository.findById(id)).thenReturn(Optional.of(existing));

        taskService.deleteTask(id);

        verify(taskRepository).delete(existing);
    }

    @Test
    void deleteTask_shouldThrowIfNotFound() {
        Long id = 1L;
        when(taskRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.deleteTask(id))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void getTasksWithStatus_shouldReturnTasksWithGivenStatus() {
        Task.TaskStatus status = Task.TaskStatus.DONE;
        LocalDate date = LocalDate.of(2025, 12, 1);
        List<Task> mockTasks = List.of(
                new Task(1L, "Task 1", "Description 1", date, status),
                new Task(2L, "Task 2", "Description 2", date, status)
        );

        when(taskRepository.findByStatus(status)).thenReturn(mockTasks);

        List<Task> result = taskService.getTasksWithStatus(status);

        assertThat(result).isEqualTo(mockTasks);
        verify(taskRepository).findByStatus(status);
    }
}
