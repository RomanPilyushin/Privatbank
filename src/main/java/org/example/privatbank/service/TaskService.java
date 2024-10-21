package org.example.privatbank.service;

import org.example.privatbank.dto.TaskDTO;
import org.example.privatbank.model.Task;
import org.example.privatbank.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List; // Import for List

/**
 * Service for handling task-related operations.
 */
@Service
public class TaskService {

    /** Repository for accessing task data */
    @Autowired
    private TaskRepository taskRepository;

    /** Service for adding tasks to the RSS feed */
    @Autowired
    private RssFeedService rssFeedService;

    /**
     * Creates a new task after performing business validations.
     *
     * @param taskDTO the task data transfer object
     * @return the created task
     */
    @Transactional
    public Task createTask(TaskDTO taskDTO) {
        // Check if the task limit (100) has been reached
        long taskCount = taskRepository.count();
        if (taskCount >= 100) {
            throw new RuntimeException("Task limit reached. Cannot create more tasks.");
        }

        // Check for duplicate tasks by title
        if (taskRepository.existsByTitle(taskDTO.getTitle())) {
            throw new RuntimeException("Task with the same title already exists");
        }

        // Convert TaskDTO to Task entity
        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setStatus(taskDTO.getStatus());

        // Save the new task to the repository
        Task savedTask = taskRepository.save(task);

        // Add the saved task to the RSS feed
        rssFeedService.addTaskToFeed(savedTask);

        // Return the saved task
        return savedTask;
    }

    /**
     * Deletes a task by its ID.
     *
     * @param id the ID of the task to delete
     */
    public void deleteTask(Long id) {
        // Delete the task from the repository
        taskRepository.deleteById(id);
    }

    /**
     * Updates the status of a task.
     *
     * @param id     the ID of the task
     * @param status the new status
     * @return the updated task
     */
    public Task updateTaskStatus(Long id, String status) {
        // Find the task by ID; throw exception if not found
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        // Update the status of the task
        task.setStatus(status);
        // Save the updated task
        return taskRepository.save(task);
    }

    /**
     * Updates specific fields of a task.
     *
     * @param id      the ID of the task
     * @param taskDTO the task data transfer object with updated fields
     * @return the updated task
     */
    @Transactional
    public Task updateTaskFields(Long id, TaskDTO taskDTO) {
        // Find the task by ID; throw exception if not found
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        // Update the title if it's provided
        if (taskDTO.getTitle() != null) {
            task.setTitle(taskDTO.getTitle());
        }
        // Update the description if it's provided
        if (taskDTO.getDescription() != null) {
            task.setDescription(taskDTO.getDescription());
        }
        // Update the status if it's provided
        if (taskDTO.getStatus() != null) {
            task.setStatus(taskDTO.getStatus());
        }

        // Save the updated task
        return taskRepository.save(task);
    }

    /**
     * Retrieves all tasks from the repository.
     *
     * @return List of all tasks
     */
    public List<Task> getAllTasks() {
        // Return all tasks
        return taskRepository.findAll();
    }
}
