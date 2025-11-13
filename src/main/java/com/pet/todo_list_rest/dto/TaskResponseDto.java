package com.pet.todo_list_rest.dto;

import com.pet.todo_list_rest.entity.Task;

import java.time.LocalDate;

public record TaskResponseDto(
        Long id,
        String title,
        String description,
        LocalDate dueDate,
        Task.TaskStatus status
) {
}
