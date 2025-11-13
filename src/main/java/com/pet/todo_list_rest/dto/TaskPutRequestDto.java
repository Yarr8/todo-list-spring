package com.pet.todo_list_rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TaskPutRequestDto(
        @NotBlank String title,
        String description,
        @NotNull LocalDate dueDate
) {

}
