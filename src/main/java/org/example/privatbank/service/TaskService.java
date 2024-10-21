package org.example.privatbank.service;

import lombok.extern.slf4j.Slf4j;
import org.example.privatbank.dto.TaskDTO;
import org.example.privatbank.model.Task;
import org.example.privatbank.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for handling task-related operations.
 */
@Slf4j
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
        log.debug("Starting task creation for: {}", taskDTO);

        // Check if the task limit (100) has been reached
        long taskCount = taskRepository.count();
        log.debug("Current task count: {}", taskCount);

        if (taskCount >= 100) {
            log.error("Task limit reached. Cannot create more tasks.");
            throw new RuntimeException("Task limit reached. Cannot create more tasks.");
        }

        // Check for duplicate tasks by title
        if (taskRepository.existsByTitle(taskDTO.getTitle())) {
            log.error("Task with the same title already exists: {}", taskDTO.getTitle());
            throw new RuntimeException("Task with the same title already exists");
        }

        // Convert TaskDTO to Task entity
        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setStatus(taskDTO.getStatus());

        // Save the new task to the repository
        Task savedTask = taskRepository.save(task);
        log.info("Task created successfully with ID: {}", savedTask.getId());

        // Add the saved task to the RSS feed
        rssFeedService.addTaskToFeed(savedTask);
        log.debug("Task added to RSS feed: {}", savedTask.getId());

        // Return the saved task
        return savedTask;
    }

    /**
     * Deletes a task by its ID.
     *
     * @param id the ID of the task to delete
     */
    public void deleteTask(Long id) {
        log.debug("Attempting to delete task with ID: {}", id);

        // Check if the task exists before trying to delete
        if (!taskRepository.existsById(id)) {
            log.error("Task with ID {} not found, cannot delete", id);
            throw new RuntimeException("Task not found");
        }

        try {
            taskRepository.deleteById(id);
            log.info("Task with ID {} deleted successfully", id);
        } catch (Exception e) {
            log.error("Error occurred while deleting task with ID: {}", id, e);
            throw e;
        }
    }

    /**
     * Check a task by its ID.
     *
     * @param id the ID of the task to check
     */
    public boolean taskExists(Long id) {
        return taskRepository.existsById(id);
    }

    /**
     * Updates the status of a task.
     *
     * @param id     the ID of the task
     * @param status the new status
     * @return the updated task
     */
    public Task updateTaskStatus(Long id, String status) {
        log.debug("Updating status of task with ID: {} to {}", id, status);

        // Find the task by ID; throw exception if not found
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Task with ID {} not found", id);
                    return new RuntimeException("Task not found");
                });

        // Update the status of the task
        task.setStatus(status);
        Task updatedTask = taskRepository.save(task);
        log.info("Task with ID {} updated to status {}", id, status);

        return updatedTask;
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
        log.debug("Updating fields of task with ID: {}", id);

        // Find the task by ID; throw exception if not found
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Task with ID {} not found", id);
                    return new RuntimeException("Task not found");
                });

        // Update the title if it's provided
        if (taskDTO.getTitle() != null) {
            log.debug("Updating title for task ID {}: {}", id, taskDTO.getTitle());
            task.setTitle(taskDTO.getTitle());
        }
        // Update the description if it's provided
        if (taskDTO.getDescription() != null) {
            log.debug("Updating description for task ID {}: {}", id, taskDTO.getDescription());
            task.setDescription(taskDTO.getDescription());
        }
        // Update the status if it's provided
        if (taskDTO.getStatus() != null) {
            log.debug("Updating status for task ID {}: {}", id, taskDTO.getStatus());
            task.setStatus(taskDTO.getStatus());
        }

        // Save the updated task
        Task updatedTask = taskRepository.save(task);
        log.info("Task with ID {} updated successfully", id);

        return updatedTask;
    }

    /**
     * Retrieves all tasks from the repository.
     *
     * @return List of all tasks
     */
    public List<Task> getAllTasks() {
        log.debug("Retrieving all tasks");

        try {
            List<Task> tasks = taskRepository.findAll();
            log.info("Retrieved {} tasks", tasks.size());
            return tasks;
        } catch (Exception e) {
            log.error("Error occurred while retrieving all tasks", e);
            throw e;
        }
    }
}
