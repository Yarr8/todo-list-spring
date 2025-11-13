package com.pet.todo_list_rest.mapper;

import com.pet.todo_list_rest.dto.TaskCreateRequestDto;
import com.pet.todo_list_rest.dto.TaskPatchStatusRequestDto;
import com.pet.todo_list_rest.dto.TaskPutRequestDto;
import com.pet.todo_list_rest.dto.TaskResponseDto;
import com.pet.todo_list_rest.entity.Task;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", expression = "java(Task.TaskStatus.TODO)")
    Task toEntity(TaskCreateRequestDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    void updateEntityFromPutDto(TaskPutRequestDto dto, @MappingTarget Task entity);

    @Mapping(target = "status", source = "status")
    @BeanMapping(ignoreByDefault = true)
    void updateEntityFromPatchStatusDto(TaskPatchStatusRequestDto dto, @MappingTarget Task entity);

    TaskResponseDto toResponseDto(Task entity);
}
