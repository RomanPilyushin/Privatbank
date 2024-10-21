package org.example.privatbank.service;

import org.example.privatbank.dto.TaskDTO;
import org.example.privatbank.model.Task;
import org.example.privatbank.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private RssFeedService rssFeedService;

    @Transactional
    public Task createTask(TaskDTO taskDTO) {
        // Limit the total number of tasks
        long taskCount = taskRepository.count();
        if (taskCount >= 100) {
            throw new RuntimeException("Task limit reached. Cannot create more tasks.");
        }

        // Check for duplicate task
        if (taskRepository.existsByTitle(taskDTO.getTitle())) {
            throw new RuntimeException("Task with the same title already exists");
        }

        // Convert TaskDTO to Task entity
        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setStatus(taskDTO.getStatus());

        Task savedTask = taskRepository.save(task);

        // Add task to RSS feed
        rssFeedService.addTaskToFeed(savedTask);

        return savedTask;
    }


    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public Task updateTaskStatus(Long id, String status) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setStatus(status);
        return taskRepository.save(task);
    }

    @Transactional
    public Task updateTaskFields(Long id, TaskDTO taskDTO) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (taskDTO.getTitle() != null) {
            task.setTitle(taskDTO.getTitle());
        }
        if (taskDTO.getDescription() != null) {
            task.setDescription(taskDTO.getDescription());
        }
        if (taskDTO.getStatus() != null) {
            task.setStatus(taskDTO.getStatus());
        }

        return taskRepository.save(task);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
}
