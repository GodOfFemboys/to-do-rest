package org.example.todorest.service;

import org.example.todorest.dto.CreateTaskDto;
import org.example.todorest.dto.PatchTaskDto;
import org.example.todorest.dto.TaskDto;
import org.example.todorest.entity.Task;
import org.example.todorest.entity.User;
import org.example.todorest.mapper.TaskMapper;
import org.example.todorest.repository.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final CustomUserDetailsService customUserDetailsService;
    private final TaskMapper taskMapper;

    public TaskServiceImpl(TaskRepository taskRepository, CustomUserDetailsService customUserDetailsService, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.customUserDetailsService = customUserDetailsService;
        this.taskMapper = taskMapper;
    }

    @Override
    public List<TaskDto> getAllTasks() {
        User user = customUserDetailsService.getCurrentUser();
        return taskRepository.findAllByUser(user).stream().map((taskMapper::toDto)).toList();
    }

    @Override
    public TaskDto getTaskById(Long id) {
        User user = customUserDetailsService.getCurrentUser();
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
        // сравниваем не id тасок, а id user-ов
        if (!task.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You can't get the task");
        }
        return taskMapper.toDto(task);
    }

    @Override
    public TaskDto saveTask(CreateTaskDto createTaskDto) {
        User user = customUserDetailsService.getCurrentUser();
        Task task = taskMapper.toEntity(createTaskDto, user);
        return taskMapper.toDto(taskRepository.save(task));
    }

    @Override
    public TaskDto patchTask(PatchTaskDto patchTaskDto, Long id) {
        User user = customUserDetailsService.getCurrentUser();
        Task task = taskRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found")); // если айди есть, но по нему ничего не находиться
        // валидация по юзеру
        if (!task.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You can't update the task"); //попытка изменить не свою таску
        }
        if (patchTaskDto.getCompleted() != null) {
            task.setCompleted(patchTaskDto.getCompleted());
        }
        if (patchTaskDto.getDescription() != null) {
            task.setDescription(patchTaskDto.getDescription());
        }
        return taskMapper.toDto(taskRepository.save(task));
    }

    @Override
    public void deleteTask(Long id) {
        User user = customUserDetailsService.getCurrentUser();
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
        // сравниваем не id тасок, а id user-ов
        if (!task.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You can't delete the task");
        }
        taskRepository.deleteById(id);
    }
}
