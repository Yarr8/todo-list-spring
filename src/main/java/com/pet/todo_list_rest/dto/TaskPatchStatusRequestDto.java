package com.pet.todo_list_rest.dto;

import com.pet.todo_list_rest.entity.Task;
import jakarta.validation.constraints.NotNull;

public record TaskPatchStatusRequestDto(
        @NotNull Task.TaskStatus status
) {
}
