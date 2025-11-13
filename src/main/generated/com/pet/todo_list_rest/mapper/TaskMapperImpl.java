package com.pet.todo_list_rest.mapper;

import com.pet.todo_list_rest.dto.TaskCreateRequestDto;
import com.pet.todo_list_rest.dto.TaskPutRequestDto;
import com.pet.todo_list_rest.dto.TaskResponseDto;
import com.pet.todo_list_rest.entity.Task;
import java.time.LocalDate;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-13T17:38:37+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class TaskMapperImpl implements TaskMapper {

    @Override
    public Task toEntity(TaskCreateRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        Task.TaskBuilder task = Task.builder();

        task.title( dto.title() );
        task.description( dto.description() );
        task.dueDate( dto.dueDate() );

        task.status( Task.TaskStatus.TODO );

        return task.build();
    }

    @Override
    public void updateEntityFromPutDto(TaskPutRequestDto dto, Task entity) {
        if ( dto == null ) {
            return;
        }

        entity.setTitle( dto.title() );
        entity.setDescription( dto.description() );
        entity.setDueDate( dto.dueDate() );
    }

    @Override
    public TaskResponseDto toResponseDto(Task entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        String title = null;
        String description = null;
        LocalDate dueDate = null;
        Task.TaskStatus status = null;

        id = entity.getId();
        title = entity.getTitle();
        description = entity.getDescription();
        dueDate = entity.getDueDate();
        status = entity.getStatus();

        TaskResponseDto taskResponseDto = new TaskResponseDto( id, title, description, dueDate, status );

        return taskResponseDto;
    }
}
