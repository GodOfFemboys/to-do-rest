package org.example.todorest.mapper;

import org.example.todorest.dto.CreateTaskDto;
import org.example.todorest.dto.TaskDto;
import org.example.todorest.entity.Task;
import org.example.todorest.entity.User;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {
    public TaskDto toDto(Task task) {
        TaskDto taskDto = new TaskDto();
        taskDto.setDescription(task.getDescription());
        taskDto.setId(task.getId());
        taskDto.setUser_id(task.getUser().getId());
        taskDto.setCompleted(task.isCompleted());
        return taskDto;
    }

    public Task toEntity(TaskDto taskDto, User user) {
        Task task = new Task();
        task.setDescription(taskDto.getDescription());
        task.setId(taskDto.getId());
        task.setUser(user);
        task.setCompleted(taskDto.isCompleted());
        return task;
    }

    public Task toEntity(CreateTaskDto createTaskDto, User user) {
        Task task = new Task();
        task.setDescription(createTaskDto.getDescription());
        task.setUser(user);
        task.setCompleted(Boolean.TRUE.equals(createTaskDto.getCompleted())); // чтобы не заставлять юзера явно указывать true/false при создании.
        return task;
    }


}
